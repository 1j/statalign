package statalign.postprocess;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import statalign.base.MainManager;
import statalign.base.Mcmc;
import statalign.base.McmcStep;
import statalign.base.State;
import statalign.base.Utils;
import statalign.mcmc.McmcModule;
import statalign.model.ext.ModelExtManager;
import statalign.ui.ErrorMessage;

/**
 * This class manages the postprocesses.
 * 
 * @author miklos, novak
 *
 */
public class PostprocessManager {
	
	/**
	 * The recognized plugins are in this array
	 */
	private List<Postprocess> plugins;
	
	/**
	 * This is the Mcmc that is analyzed
	 */
	public Mcmc mcmc;
	
	/**
	 * This is the log-file writer 
	 */
	public FileWriter logFile = null;
	
	/**
	 * This is the Main manager that manages the MCMC run.
	 */
	public MainManager mainManager;
	
	/**
	 * 
	 */
	public static boolean rnaMode = false;
	
	/**
	 * Static object holding parameters, which are visible to all plugins.
	 */
	public static PluginParameters pluginParameters = new PluginParameters();

	/** The base filename where output is to be sent. Each plugin appends a suffix to this. */ 
	private String baseFileName;
	public String getBaseFileName() { return baseFileName; }
	public void setBaseFileName(String s) { baseFileName = s; }
	
	/**
	 * This constructor recognizes the plugins
	 * @param mainManager The MainManager that manages the MCMC run.
	 */
	public PostprocessManager(MainManager mainManager, boolean parallel) {
		this.mainManager = mainManager;
		List<String> pluginNames = Utils.classesInPackage(Postprocess.class.getPackage().getName()+".plugins");
		HashMap<String,Integer> nameMap = new HashMap<String,Integer>();
		HashMap<Postprocess,Boolean> selectedMap = new HashMap<Postprocess, Boolean>();
		plugins = new ArrayList<Postprocess>(pluginNames.size());
		for(int i = 0; i < pluginNames.size(); i++)
			plugins.add(null);
		for(int i = 0; i < pluginNames.size(); i++) {
			nameMap.put(pluginNames.get(i), i);
			try {
				Class<?> cl = Class.forName(pluginNames.get(i));
				if(!Postprocess.class.isAssignableFrom(cl))
					continue;
				Postprocess plugin = (Postprocess)cl.newInstance();
				selectedMap.put(plugin, plugin.selected);
				plugin.selected = plugin.active = true;
				plugins.set(i, plugin);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ArrayList<Postprocess> workingPlugins = new ArrayList<Postprocess>();
		for(Postprocess plugin : plugins) 
			dependProb(plugin, nameMap, workingPlugins);
					
		// reassign the list of working plugins
		plugins = workingPlugins;
		
		for(Postprocess plugin : plugins) {
			// If we're running in parallel mode, deactivate the plugins that are not used
			if (parallel && !plugin.useInParallelMode) {
				plugin.active = false;
				plugin.postprocessWrite = false;
				// If at some point we want to add cold-chain output to GUI, for example,
				// we could activate just the master process.
			}
			else plugin.active = true;
		}
		
		boolean show = mainManager.frame != null;
		for(Postprocess plugin : plugins){
			plugin.selected = selectedMap.get(plugin);
			if(plugin.screenable) {
				plugin.show = show;
			}
//			plugin.init();
		}
	}
	
	public List<Postprocess> getPlugins() {
		return Collections.unmodifiableList(plugins);
	}
	
	public void createPluginFiles() {
		try{
		for (Postprocess p : plugins) {			
			if (p.postprocessWrite) {				
				String name = baseFileName + p.getFileExtension();
				System.out.println("Output file for " + p.getTabName()
						+ ": " + name);
				p.outputFile = new FileWriter(name);
				if (p.createsMultipleOutputFiles()) {
					for (String extension : p.getAdditionalFileExtensions()) {
						name = baseFileName + extension;
					}
					System.out.println("Additional output file for " + p.getTabName()
							+ ": " + name);
					p.additionalOutputFiles = new ArrayList<FileWriter>();
					p.additionalOutputFiles.add(new FileWriter(name));
				}					
			}
		}		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Finds dependency problems. Adds working plugins in dependency order to workingPlugins.
	 */
	private boolean dependProb(Postprocess plugin, HashMap<String,Integer> nameMap, ArrayList<Postprocess> workingPlugins) {
		if(plugin == null)
			return true;
		if(!plugin.selected)
			return plugin.active;
		plugin.selected = false;
		String[] deps = plugin.getDependencies();
		Postprocess[] refs = null;
		if(deps != null) {
			refs = new Postprocess[deps.length];
			for(int i = 0; i < deps.length; i++) {
				Integer ind = nameMap.get(deps[i]);
				if(ind == null || dependProb(plugins.get(ind), nameMap, workingPlugins))
					return true;
				refs[i] = plugins.get(ind);
			}
		}
		plugin.refToDependencies(refs);
		plugin.active = false;
		workingPlugins.add(plugin);
		return false;
	}
	
	public void init(ModelExtManager modelExtMan) {
		for(Postprocess plugin : plugins){
			plugin.postprocessManager = this;
			plugin.init(modelExtMan);
		}
	}
	/**
	 * This function invoked before the first sample.
	 * It opens information chanels towards postrocessing plug-ins.
	 */
	public void beforeFirstSample() {
		
		if(rnaMode) {
			for(Postprocess plugin : plugins){
				plugin.mcmc = mcmc; // TODO Change RNA plugins so this is not needed
				plugin.file = logFile;
				plugin.alignmentType = MainManager.alignmentTypes[mainManager.inputData.currentAlignmentType];
				plugin.beforeFirstSample(mainManager.inputData);
			}
		}
		
		else {
			for(Postprocess plugin : plugins) {
				if(!plugin.rnaAssociated) {					
					plugin.file = logFile;
					plugin.alignmentType = MainManager.alignmentTypes[mainManager.inputData.currentAlignmentType];
					plugin.beforeFirstSample(mainManager.inputData);
				}
			}
		}
	}
	
	/**
	 * Calls the plug-ins at a new sample.
	 * @param no The number of the current sample
	 * @param total The total number of samples.
	 */
	public void newSample(McmcModule coreModel, State state, int no, int total) {
		if(rnaMode) {
			for(Postprocess plugin : plugins) {
				plugin.newSample(coreModel,state, no, total);
			}
		}
		
		else {
			for(Postprocess plugin : plugins) {
				if(!plugin.rnaAssociated) {
					plugin.newSample(coreModel,state, no, total);
				}
			}
		}
	}
	
	/**
	 * Calls the plug-ins after an MCMC step.
	 */
	public void newStep(McmcStep step) {
		if(rnaMode) {
			for(Postprocess plugin : plugins){
				//if(plugin.selected){
					plugin.newStep(step);
				//}
			}
		}
		
		else {
			for(Postprocess plugin : plugins){
				if(!plugin.rnaAssociated){
					plugin.newStep(step);
				}
			}
		}
	}
	
	public void flushAll() {
		if(rnaMode) {
			for(Postprocess plugin : plugins){
				if(plugin.selected && plugin.postprocessWrite){
					try { plugin.outputFile.flush(); }
					catch (IOException ioex) {
						ioex.printStackTrace();
					} 
				}
			}
		}
		
		else {
			for(Postprocess plugin : plugins){
				if(plugin.selected && plugin.postprocessWrite && 
						!plugin.rnaAssociated){
					try { plugin.outputFile.flush(); }
					catch (IOException ioex) {
						ioex.printStackTrace();
					}
				}
			}
		}		
	}
	/**
	 * Calls the plug-ins after an MCMC step.
	 */
	public void newPeek() {
		State state = mcmc.getState();
		if(rnaMode) {
			for(Postprocess plugin : plugins){
				//if(plugin.selected){
					plugin.newPeek(state);
				//}
			}
		}
		
		else {
			for(Postprocess plugin : plugins){
				if(!plugin.rnaAssociated){
					plugin.newPeek(state);
				}
			}
		}
	}
	
	/**
	 * It is called after the last sample of MCMC.
	 * It calls plug-ins to finalize their postprocessing activities.
	 */
	public void afterLastSample() {
		if(rnaMode) {			
			for(Postprocess plugin : plugins) {					
				plugin.afterLastSample();	
			}
		}
		else {
			for(Postprocess plugin : plugins) {
				if(!plugin.rnaAssociated) {
					plugin.afterLastSample();
				}
			}
		}			
		try {
			logFile.close();
		} catch (IOException e) {
			new ErrorMessage(mainManager.frame, " "+e.getLocalizedMessage(), true);
		}
	}

	public void updateSelectedList() {
		if(mainManager.frame != null)
			mainManager.frame.updateTabs();
	}
	
}

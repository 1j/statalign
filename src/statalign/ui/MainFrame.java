package statalign.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import statalign.StatAlign;
import statalign.base.Input;
import statalign.base.MainManager;
import statalign.base.Utils;
import statalign.io.DataType;
import statalign.io.RawSequences;
import statalign.model.ext.ModelExtension;
import statalign.model.subst.RecognitionError;
import statalign.model.subst.SubstitutionModel;
import statalign.model.subst.plugins.Dayhoff;
import statalign.model.subst.plugins.Kimura3;
import statalign.postprocess.Postprocess;

/**
 * The main frame of the program.
 * Created by <code>statalign.StatAlign</code> when run in graphical mode.
 * @author miklos, novak
 */
public class MainFrame extends JFrame implements ActionListener {

    // Constants

	public static final String IDLE_STATUS_MESSAGE = " StatAlign :: Ready";
	private static final long serialVersionUID = 1L;

	  public static final String WELCOME_MSG = 
		          "<html><div style='padding: 20px 20px 20px 20px; font-family: Arial; font-size: 12px'>" +
		        "<h2>Welcome to StatAlign!</h2><br>" +
		        "<p>To get started, please add sequences to analyse." +
		        "<p>If you need any help along the way, please refer to the <a href='http://doc'>manual</a> that is also available from the <b>Help menu.</b>" +
		        "<br><br>" +
		        "<p>Happy StatAligning!" +
		  //      "<div style='padding: 20px 100px 0px 0px' align=right>" +
		  //      "<p><i>The StatAlign team</i>" +
		        "</div></html>";
    // Variables

    private JTabbedPane tab;
    private List<Postprocess> pluginTabs;
    private Input input;
    
    public JToolBar toolBar;
    private JButton openButton;
    private JButton runButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JButton stopButton;
    
    public JToggleButton rnaButton;
    // TODO The above should be inside the RNA plugin classes rather than here
    
    private JMenuItem openItem;
    private JMenuItem runItem;
    private JMenuItem pauseItem;
    private JMenuItem resumeItem;
    private JMenuItem stopItem;

    private JMenuItem[] modelButtons;

    public JLabel statusText;

    private HashMap<Integer, Postprocess> tabPluginMap;
    private Integer lastSelectedTabIndex = -1;
    private ArrayList<JTabbedPane> allTabs;

    /** The main manager that handles the MCMC run. */
    public MainManager manager;

    private McmcSettingsDlg mcmcSettingsDlg;
    
    private File inFile;
    private Class<? extends SubstitutionModel>[] substModels;

    // Functions
    OutputPreferences op;
    RNASettingsDlg dlg = new RNASettingsDlg(this);
    
    /** The only constructor of the class. It launches the main window. */
    @SuppressWarnings("unchecked")
    public MainFrame() throws Exception {
        super("StatAlign " + StatAlign.version);

        try {
            String syslook = UIManager.getSystemLookAndFeelClassName();
            if (!UIManager.getLookAndFeel().getClass().getName().equals(syslook)) {
                UIManager.setLookAndFeel(syslook);
                SwingUtilities.updateComponentTreeUI(this);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
        }

        ArrayList<Class<?>> substModList = new ArrayList<Class<?>>();
        for (String model : Utils.classesInPackage(SubstitutionModel.class.getPackage().getName() + ".plugins")) {
            try {
                Class<?> cl = Class.forName(model);
                /* Only include non-abstract substitution models that extend SubstitutionModel */
                if (!Modifier.isAbstract(cl.getModifiers()) &&
                        SubstitutionModel.class.isAssignableFrom(cl))
                    substModList.add(cl);
            } catch (Exception ex) {
                ErrorMessage.showPane(null, ex, true);
            }
        }

        substModels = (Class<? extends SubstitutionModel>[]) substModList.toArray(new Class<?>[substModList.size()]);
        manager = new MainManager(this);

        manager.init(null);
        //mcmcSettingsDlg = new McmcSettingsDlg(this);

        setMinimumSize(new Dimension(500, 250));

        ///
        /// Creates the MenuBar and the ToolBar
        ///

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        JMenuBar menubar = new JMenuBar();

        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        JMenuItem item;

        String openText = "Add sequence(s)...";
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("icons/open.png"));
        
        openItem = createMenuItem(openText, true);
        openItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
        openItem.setMnemonic(KeyEvent.VK_A);
        openItem.setIcon(icon);
        menu.add(openItem);

        openButton = createButton(new ImageIcon(ClassLoader.
        		getSystemResource("icons/open.png")), "Add sequence(s)...");
        openButton = createButton(icon, "Add sequence(s)...");
        toolBar.add(openButton);

        toolBar.addSeparator();
        
        for(ModelExtension plugin : manager.modelExtMan.getPluginList()) {
        	for(JComponent comp : plugin.getToolBarItems())
        		toolBar.add(comp);
        }
        
        String rnaText = "RNA mode";
        rnaButton = createToggleButton(new ImageIcon(ClassLoader.getSystemResource("icons/rna1.png")), rnaText);
        rnaButton.setEnabled(false);
        rnaButton.setSelected(false);
        toolBar.add(rnaButton);
        
        mcmcSettingsDlg = new McmcSettingsDlg(this);
        
        toolBar.addSeparator();

        JMenuItem ioPreferencesItem = createMenuItem("Preferences...", true);
        ioPreferencesItem.setAccelerator(KeyStroke.getKeyStroke("control 1"));
        ioPreferencesItem.setMnemonic(KeyEvent.VK_P);
        menu.add(ioPreferencesItem);

        menu.addSeparator();

        item = createMenuItem("Exit", true);
        item.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
        item.setMnemonic(KeyEvent.VK_X);
        menu.add(item);
        menubar.add(menu);

//		menu = new JMenu("Edit");
//		menu.setMnemonic(KeyEvent.VK_E);
//		item = new JMenuItem("Cut");
//		item.addActionListener(this);
//		item.setAccelerator(KeyStroke.getKeyStroke("control X"));
//		menu.add(item);
//		item = new JMenuItem("Copy");
//		item.addActionListener(this);
//		item.setAccelerator(KeyStroke.getKeyStroke("control C"));
//		menu.add(item);
//		item = new JMenuItem("Paste");
//		item.addActionListener(this);
//		item.setAccelerator(KeyStroke.getKeyStroke("control V"));
//		menu.add(item);
//		menubar.add(menu);


        menu = new JMenu("Analysis");
        menu.setMnemonic(KeyEvent.VK_M);

//        item = createMenuItem("Settings", true);
//        item.setAccelerator(KeyStroke.getKeyStroke("control M"));
//        item.setMnemonic(KeyEvent.VK_S);
//        menu.add(item);


        String runText = "Run";
        icon = new ImageIcon(ClassLoader.getSystemResource("icons/play.png"));
        runItem = createMenuItem(runText, false);
        runItem.setAccelerator(KeyStroke.getKeyStroke("control ENTER"));
        runItem.setIcon(icon);
        menu.add(runItem);
        
        runButton = createButton(icon, runText);
        menu.addSeparator();
        runButton.setEnabled(false);
        toolBar.add(runButton);

        String pauseText = "Pause";
        icon = new ImageIcon(ClassLoader.getSystemResource("icons/pause.png"));
        pauseItem = createMenuItem("Pause", false);
        pauseItem.setIcon(icon);
        menu.add(pauseItem);

        pauseButton = createButton(icon, pauseText);
        pauseButton.setEnabled(false);
        toolBar.add(pauseButton);


        String resumeText = "Resume";
        icon = new ImageIcon(ClassLoader.getSystemResource("icons/resume.png"));
        resumeItem = createMenuItem(resumeText, false);
        resumeItem.setIcon(icon);
        menu.add(resumeItem);

        resumeButton = createButton(icon, resumeText);
        resumeButton.setEnabled(false);
        toolBar.add(resumeButton);


        String stopText = "Stop";
        icon = new ImageIcon(ClassLoader.getSystemResource("icons/stop.png"));
        stopItem = createMenuItem("Stop", false);
        stopItem.setIcon(icon);
        menu.add(stopItem);
        menubar.add(menu);

        stopButton = createButton(icon, stopText);
        stopButton.setEnabled(false);
        toolBar.add(stopButton);
        
        String settingsText = "Settings";
        JButton settingsButton = createButton(new ImageIcon(ClassLoader.
        		getSystemResource("icons/settings.png")), settingsText);
        toolBar.add(settingsButton);

        menu = new JMenu("Substitution models");
        menu.setMnemonic(KeyEvent.VK_L);

        modelButtons = new JMenuItem[substModels.length];
        ButtonGroup modelGroup = new ButtonGroup();
        HashMap<String, ArrayList<Class<? extends SubstitutionModel>>> substModTypes =
                new HashMap<String, ArrayList<Class<? extends SubstitutionModel>>>();
        for (Class<? extends SubstitutionModel> cl : substModels) {
            String type = SubstitutionModel.getType(cl);
            ArrayList<Class<? extends SubstitutionModel>> arr = substModTypes.get(type);
            if (arr == null)
                substModTypes.put(type, arr = new ArrayList<Class<? extends SubstitutionModel>>());
            arr.add(cl);
        }
        String[] typeArr = new String[substModTypes.keySet().size()];
        int s = 0;
        if (typeArr.length >= 2) {
            typeArr[0] = "protein";                // amino acid subst models first
            typeArr[1] = "nucleotide";        // then nucleotide models, then the rest
            s = 2;
        }
        for (String type : substModTypes.keySet()) {
            if (!type.equals("protein") && !type.equals("nucleotide"))
                typeArr[s++] = type;
        }
        s = 0;
        for (String type : typeArr) {
            if (s > 0)
                menu.addSeparator();
            for (Class<? extends SubstitutionModel> cl : substModTypes.get(type)) {
                String name = SubstitutionModel.getMenuName(cl);
                item = new JRadioButtonMenuItem(name);
                item.addActionListener(this);
                modelButtons[s++] = item;
                modelGroup.add(item);
                menu.add(item);
            }
        }
        modelGroup.clearSelection();
        menubar.add(menu);

        /*menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_V);
        JMenuItem zoomInItem = new JMenuItem("Zoom in");
        zoomInItem.setEnabled(false);
        zoomInItem.addActionListener(this);
        menu.add(zoomInItem);
        JMenuItem zoomOutItem = new JMenuItem("Zoom out");
        zoomOutItem.setEnabled(false);
        zoomOutItem.addActionListener(this);
        menu.add(zoomOutItem);
        JMenuItem autofitItem = new JMenuItem("Fit to frame");
        autofitItem.setEnabled(false);
        autofitItem.addActionListener(this);
        menu.add(autofitItem);

        menubar.add(menu);*/

        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        item = new JMenuItem("About...");
        item.addActionListener(this);
        menu.add(item);
        menu.addSeparator();
        item = new JMenuItem("Help for users");
        item.addActionListener(this);
        menu.add(item);
        menu.addSeparator();
        item = new JMenuItem("Html doc for developers");
        item.addActionListener(this);
        menu.add(item);
        item = new JMenuItem("Description of plugins");
        item.addActionListener(this);
        menu.add(item);

        menubar.add(menu);

        setJMenuBar(menubar);
        add(toolBar, BorderLayout.PAGE_START);

        ///
        /// Creates the main panel
        ///

        Container cp = getContentPane();
        JPanel mainPanel = new JPanel(new BorderLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainPanel.setMinimumSize(new Dimension(screenSize.width / 3, screenSize.height / 3));
        mainPanel.setMaximumSize(new Dimension(screenSize.width, screenSize.height));
        mainPanel.setPreferredSize(new Dimension(screenSize.width / 2, screenSize.height / 2));

        tab = new JTabbedPane();

        input = new Input(manager);
        tab.addTab(input.getTabName(), input.getIcon(), input.getJPanel(), input.getTip());
        manager.inputgui = input.inputgui;

        // Sorts the tab according to their getTabOrder()
        pluginTabs = new ArrayList(manager.postProcMan.getPlugins());
        Collections.sort(pluginTabs, new Comparator<Postprocess>() {
            @Override
            public int compare(Postprocess firstTab, Postprocess secondTab) {
                return Double.compare(firstTab.getTabOrder(), secondTab.getTabOrder());
            }
        });

        tabPluginMap = new HashMap<Integer, Postprocess>();
        for (int i = 0; i < pluginTabs.size(); i++) {
            Postprocess plugin = pluginTabs.get(i);
            if (plugin.selected && !plugin.rnaAssociated) {
                tabPluginMap.put(i + 1, plugin); // TODO: Jesus Java, horrible.
                tab.addTab(plugin.getTabName(), plugin.getIcon(), plugin.getJPanel(), plugin.getTip());
            }
        }

        tab.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane pane = (JTabbedPane) changeEvent.getSource();
                Integer i = pane.getSelectedIndex();

                // Remove the last tabs toolbar items.
                if (lastSelectedTabIndex != -1) {
                    Postprocess lastPlugin = tabPluginMap.get(lastSelectedTabIndex);
                    if (lastPlugin != null && lastPlugin.hasToolBar) {
                        for (JComponent item : lastPlugin.getToolBarItems()) {
                            toolBar.remove(item);
                        }
                        toolBar.revalidate(); // TODO: optimize.
                        toolBar.repaint();
                    }
                }

                // Add the currents tab toolbar items.
                Postprocess plugin = tabPluginMap.get(i);
                if (plugin != null && plugin.hasToolBar) {
                    for (JComponent item : plugin.getToolBarItems()) {
                        toolBar.add(item);
                    }
                    toolBar.revalidate();
                    toolBar.repaint();
                }
                lastSelectedTabIndex = i;
            }
        });

        mainPanel.add(tab, BorderLayout.CENTER);

        cp.add(mainPanel, BorderLayout.CENTER);
        JPanel statusBar = new JPanel(new BorderLayout());
        statusText = new JLabel(IDLE_STATUS_MESSAGE);
        statusBar.add(statusText, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.SOUTH);

//		setSize(300, 200);
//		setLocationByPlatform(true);
//    	setLocation(screenSize.width/4,screenSize.height/4);
        pack();
        setBounds(screenSize.width / 5 - 15, screenSize.height / 5 - 15, screenSize.width * 3 / 5 + 30, screenSize.height * 3 / 5 + 30);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JMenuItem createMenuItem(String text, boolean enabled) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(this);
        item.setEnabled(enabled);
        return item;
    }

    private JButton createButton(Icon icon, String text) {
        JButton button = new JButton(icon);
        button.setToolTipText(text);
        button.addActionListener(this);
        button.setActionCommand(text);
        return button;
    }
    
    private JToggleButton createToggleButton(Icon icon, String text) {
    	JToggleButton button = new JToggleButton(icon);
    	button.setToolTipText(text);
    	button.addActionListener(this);
    	button.setActionCommand(text);
    	return button;
    }

    /** An ActioListener is implemented, so we have to implement this function. It handles actions on the menu bar. */
    @Override
	public void actionPerformed(ActionEvent ev) {
        if (ev.getActionCommand() == "Add sequence(s)...") {
            JFileChooser choose = new JFileChooser("Add sequence(s)...");
            choose.setCurrentDirectory(new File(System.getProperty("user.dir")));
            choose.setMultiSelectionEnabled(true);
            if (choose.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            	for(File file : choose.getSelectedFiles()) {
	                DataType data = manager.dataMan.read(file);
	                if(data == null) {
	                	JOptionPane.showMessageDialog(this, "The following file was not recognised to be in a known format:\n"+file+"\n\n", "Error reading input file", JOptionPane.ERROR_MESSAGE);
	                	continue;
	                }
	                if(data instanceof RawSequences) {
	                	if(inFile == null)
	                		inFile = file;
	                	manager.inputData.seqs.add((RawSequences)data);
	                	manager.inputgui.updateSequences();
	                	manager.fullPath = inFile.getAbsolutePath();
	                	if (manager.inputData.model != null) {
	                		try {
	                			manager.inputData.model.acceptable(manager.inputData.seqs);
	                		} catch (RecognitionError e) {
	                			tryModels();
	                		}
	                	} else {
	                		tryModels();
	                	}
	                	if (manager.inputData.model != null) {
	                		runItem.setEnabled(true);
	                		runButton.setEnabled(true);
	                		
	                		if(!manager.inputData.seqs.isRNA()) {
	                			rnaButton.setEnabled(false);
	                		} else { 
	                			rnaButton.setEnabled(true);
	                		}
	                	}
	                } else {
	                	// TODO add Tree type as in console version
	                	manager.inputData.auxData.add(data);
	                	manager.inputgui.updateSequences();
	                }
//	                FileFormatReader reader = new FastaReader();
//	                try {
//	                	manager.inputData.seqs.alphabet = "";
//	                	manager.inputData.seqs.add(reader.read(inFile));
//	                	manager.inputgui.updateSequences();
//	                	manager.fullPath = inFile.getAbsolutePath();
//	                	if (manager.inputData.model != null) {
//	                		try {
//	                			manager.inputData.model.acceptable(manager.inputData.seqs);
//	                		} catch (RecognitionError e) {
//	                			tryModels();
//	                		}
//	                	} else {
//	                		tryModels();
//	                	}
//	                	if (manager.inputData.model != null) {
//	                		runItem.setEnabled(true);
//	                		runButton.setEnabled(true);
//
//	                		if(!manager.inputData.seqs.isRNA()) {
//	                			rnaButton.setEnabled(false);
//	                		}
//
//	                		else { 
//	                			rnaButton.setEnabled(true);
//	                		}
//	                	}
//	                } catch (IOException e) {
//	                	JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error reading input file", JOptionPane.ERROR_MESSAGE);
//	                } catch (ExceptionNonFasta e) {
//	                	// TODO Auto-generated catch block
//	                	ErrorMessage.showPane(this, e.getMessage(), true);
//	                }
            	}           
            }
            
        } else if (ev.getActionCommand() == "Exit") {
            System.exit(0);
        } else if (ev.getActionCommand() == "Preferences...") {
            //System.out.println("here!!!");
            op = new OutputPreferences(this);
        } else if (ev.getActionCommand() == "Settings") {
            mcmcSettingsDlg.display(this);
        } else if (ev.getActionCommand() == "Run") {
        	if (manager.inputData.seqs.sequences.size() < 2) {
                JOptionPane.showMessageDialog(this, "At least two sequences are needed!!!",
                        "Not enough sequences", JOptionPane.ERROR_MESSAGE);
//    			manager.finished();
                return;
            }
        	disableAllButtons();
        	start();
            
            
        } else if (ev.getActionCommand() == "Pause") {
            pauseItem.setEnabled(false);
            pauseButton.setEnabled(false);

            resumeItem.setEnabled(true);
            resumeButton.setEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            final String savTit = getTitle();
            setTitle("Pausing...");
            manager.thread.suspendSoft();
            setTitle(savTit);
            setCursor(Cursor.getDefaultCursor());
        } else if (ev.getActionCommand() == "Resume") {
            manager.thread.resumeSoft();
            pauseItem.setEnabled(true);
            pauseButton.setEnabled(true);
            resumeItem.setEnabled(false);
            resumeButton.setEnabled(false);
        } else if (ev.getActionCommand() == "Stop") {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            final String savTit = getTitle();
            setTitle("Stopping...");
            manager.thread.stopSoft();
            finished();
            setTitle(savTit);
            setCursor(Cursor.getDefaultCursor());
        } else if (ev.getActionCommand() == "RNA mode") {
        	if(rnaButton.isSelected()) {
           		dlg = new RNASettingsDlg (this);
        		dlg.display(this);
        		
        		//manager.inputgui = input.inputgui;
				//manager.inputgui.updateSequences();
				//tab.addTab(input.getTabName(), input.getIcon(), input.getJPanel(), input.getTip());
				manager.inputgui = input.inputgui;
				manager.inputgui.updateSequences();
				//System.out.println("SELECTED!!!");
				
				manager.postProcMan.rnaMode = true;
				//manager.postProcMan.reload();
   
				int count = tab.getTabCount();
				for(Postprocess plugin : pluginTabs) {
					//System.out.println(plugin.getTabName() + ": " + plugin.screenable);
					if(plugin.rnaAssociated) {
						tabPluginMap.put(count, plugin);
						tab.addTab(plugin.getTabName(), plugin.getIcon(), plugin.getJPanel(), plugin.getTip());
						count++;
					}
					
					//manager.postProcMan.init();
				}
        		
        		}
        	
        	
        	else {
        		
        		manager.postProcMan.rnaMode = false;
        		//System.out.println("NOT SELECTED!!!");
        		manager.inputgui = input.inputgui;
        		manager.inputgui.updateSequences();
        		
        		int count = 0;
        		for(Postprocess plugin : pluginTabs) {
        			if(plugin.rnaAssociated) {
        				tabPluginMap.remove(plugin);
        				String removePlugin = tab.getTitleAt(count+1);
        				tab.remove(count + 1);
        				count--;
        			}
        			
        			count++;
        			
        				
        		}
        		
        		
        	}
    
        	
        } else if (ev.getActionCommand() == "About...") {
            new HelpWindow(this, "About", getClass().getClassLoader().getResource("doc/about/index.html"), false);
        } else if (ev.getActionCommand() == "Html doc for developers") {
            new HelpWindow(this, "Html doc for Developers", ClassLoader.getSystemResource("doc/index.html"), true);
        } else if (ev.getActionCommand() == "Description of plugins") {
            new HelpWindow(this, "Description of plugins",
                    ClassLoader.getSystemResource("doc/plugin_description/index.html"), true);

        } else if (ev.getActionCommand() == "Help for users") {
        	 helpUsers();
        } else {        // new substitution model selected
            for (Class<? extends SubstitutionModel> cl : substModels) {
                try {
                    if (ev.getActionCommand().equals(SubstitutionModel.getMenuName(cl))) {
                        try {
                            SubstitutionModel model = cl.newInstance();
                            model.acceptable(manager.inputData.seqs);
                            manager.inputData.model = model;
                            break;
                        } catch (RecognitionError e) {
                            selectModel(manager.inputData.model);
                            JOptionPane.showMessageDialog(this, e.message, "Cannot apply this model...", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    }
                } catch (Exception e) {
                    new ErrorMessage(this, e.getLocalizedMessage(), true);
                }
            }

        }
    }

    public void helpUsers() {
    	try {	
    		File dir = new File(System.getProperty("user.dir")+"/doc/help/index.html");
    		Desktop.getDesktop().browse(dir.toURI());
    	} catch (Exception e) {
    		new HelpWindow(this, "Help for users",
    				ClassLoader.getSystemResource("doc/help/index.html"), true);    
    	    }
    }	
    private String tryModels() {
        String message = "";
        try {
            SubstitutionModel[] defaultSubstList = {
                    new Kimura3(),
                    new Dayhoff()
            };
            for (SubstitutionModel model : defaultSubstList) {
                try {
                    model.acceptable(manager.inputData.seqs);
                    selectModel(model);
                    break;
                } catch (RecognitionError e) {
                }
            }
            if (manager.inputData.model == null) {
                double max = 0.0;
                int wrong = 0;
                for (Class<? extends SubstitutionModel> cl : substModels) {
                    SubstitutionModel m;
                    try {
                        m = cl.newInstance();
                        if (m.acceptable(manager.inputData.seqs) > max) {
                            manager.inputData.model = m;
                            max = m.acceptable(manager.inputData.seqs);
                            selectModel(m);
                        }
                    } catch (RecognitionError e) {
                        wrong++;
                        message += e.message;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error accessing substitution models", JOptionPane.ERROR_MESSAGE);
        }
        return message;

    }

    private void selectModel(SubstitutionModel m) {
        manager.inputData.model = m;
        for (JMenuItem mi : modelButtons) {
            if (mi.getText().equals(m.getMenuName())) {
                mi.setSelected(true);
                break;
            }
        }
    }

    /** Enables several menu items that were kept disabled during the run. */
    public void finished() {
        openItem.setEnabled(true);
        openButton.setEnabled(true);
        runItem.setEnabled(true);
        runButton.setEnabled(true);
        rnaButton.setEnabled(true);
        
        //rnaButton.setSelected(false);
        pauseItem.setEnabled(false);
        pauseButton.setEnabled(false);
        resumeItem.setEnabled(false);
        resumeButton.setEnabled(false);
        stopItem.setEnabled(false);
        stopButton.setEnabled(false);
        
        statusText.setText(MainFrame.IDLE_STATUS_MESSAGE);
        //SavedFilesPopup.showPane(this);
		
    }
    
    public void deactivateRNA() {
    	
    	int count = 0;
    	if(manager.postProcMan.rnaMode) {
			for(Postprocess plugin : pluginTabs) {
				if(plugin.rnaAssociated) {
					plugin.reloadPanel();
					tabPluginMap.remove(plugin);
					tab.remove(count + 1);
					count--;
				}
				
				count++;		
			}
    	}
    	
    	manager.postProcMan.rnaMode = false;
    	rnaButton.setSelected(false);
    	rnaButton.setEnabled(false);
    }
    
    public void disableAllButtons() {
    	openItem.setEnabled(false);
        openButton.setEnabled(false);
        runItem.setEnabled(false);
        runButton.setEnabled(false);

        pauseItem.setEnabled(true);
        pauseButton.setEnabled(true);

        resumeItem.setEnabled(false);

        stopItem.setEnabled(true);
        stopButton.setEnabled(true);
        
        rnaButton.setEnabled(false);
    }
    
    public void start() {
        //runSettings.display(this);
        manager.start();
    }
    

    /**
     * Merely for testing purposes.
     * @param args No argument is used.
     */
    public static void main(String[] args) throws Exception {
        new MainFrame();
    }

}
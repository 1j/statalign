package statalign.postprocess.plugins.contree.test;

import java.io.IOException;
import java.util.List;

import statalign.postprocess.plugins.TreeNode;
import statalign.postprocess.plugins.contree.CTMain;
import statalign.postprocess.plugins.contree.CTree;
import statalign.postprocess.utils.NewickParser;

public class CorrectnessTester {

    public static String prefix;

    public static void main(String[] args) throws IOException {

        CorrectnessTester correctnessTester = new CorrectnessTester();
        CorrectnessTester.prefix = args.length != 0 ? args[0] : "/home/krummi/Desktop/HashCS/HashCS/examples/";
        String hashCS;

        ///
        /// 150-taxa-10-trees.tre
        hashCS = "(3,1,(2,((((14,(11,((21,(19,(20,24)int87)int65)int55,(18,(22,23)int86)int64,(146,147)int85)int33)int30,(27,(10,25)int88)int66,(15,16)int89)int22,(17,26)int90)int18,((4,6)int91,(9,(13,(5,((((49,((67,90)int101,(83,(76,(118,(150,(127,(((112,82)int104,(71,93)int105)int63,((139,137,138)int73,((85,86)int102,(97,(120,((59,84)int103,(94,(88,98)int121)int81)int49)int45)int42)int35)int28)int23)int20)int19)int17,((103,104)int106,(87,(140,(99,(141,(80,(100,96)int120)int74)int58,((109,123)int107,(125,81)int108)int59)int34)int32)int29)int27)int13,(149,(126,148)int125)int72)int12)int11,(117,(124,(95,(122,78)int109)int75)int60)int54)int10,(92,(57,(((((102,110)int117,(106,79)int116)int62,(70,(119,121)int118)int79)int44,((115,91)int114,((130,42)int115,(89,(114,(116,131)int124)int78)int61)int46)int38)int24,(133,53)int113)int21,(74,(73,128,((129,136,132)int76,(135,72)int110)int50)int43)int37,((105,113)int111,(108,(29,69)int112)int77)int51)int15)int14)int9,((111,(101,77)int100)int71,((37,28,(32,30,31)int84)int48,((33,36)int98,(38,(34,35)int99)int82)int53)int31)int26,(107,75)int97)int8,((144,((64,65)int94,(66,(143,(54,(142,48)int93)int68)int56)int47)int40,(58,(43,68)int96)int80,(56,(44,47)int95)int83)int25,(45,((40,(12,52)int122)int70,(60,(134,63,145)int69)int57)int41)int36,((51,41,61,(50,62)int123)int52,(46,(39,55)int92)int67)int39)int16)int7)int6)int5)int4)int3)int2,(7,8)int119)int1)int0)root;";
        correctnessTester.test("data/150-taxa-10-trees.tre", hashCS, 10);

        ///
        /// 150-taxa-100-trees.tre
        hashCS = "(3,1,(2,(((((11,(((146,147)int116,(21,(19,(20,24)int117)int80)int64)int48,(18,(22,23)int115)int79)int33)int31,(14,((15,16)int119,(27,(10,25)int118)int81)int56)int49)int23,(17,26)int114)int18,((4,6)int113,(9,(13,(5,(((107,75)int93,(((111,(101,77)int94)int70,((28,37,(31,30,32)int88)int51,((33,36)int95,(38,(34,35)int96)int84)int55)int30)int27,((49,((67,90)int104,(83,76,(118,(150,(127,(((112,82)int108,(71,93)int109)int67,((137,139,138)int77,((85,86)int110,(97,(120,((59,84)int111,(94,(88,98)int127)int83)int52)int47)int43)int34)int28)int22)int20)int19)int17,(((103,104)int107,(149,(126,148)int125)int76)int57,(87,140,(99,(((109,123)int106,(125,81)int105)int61,(141,(80,(100,96)int124)int75)int62)int38)int32)int29)int24)int13)int12,(117,(124,(95,(122,78)int112)int78)int63)int54)int11,(92,(57,(((((102,110)int102,(106,79)int101)int66,(70,(119,121)int103)int74)int44,((115,91)int100,((130,42)int121,(89,(116,131,114)int73)int65)int46)int35)int25,(133,53)int99)int21,(74,((128,73)int128,((136,132,129)int72,(135,72)int123)int53)int42)int37,((105,113)int98,(108,(29,69)int97)int71)int58)int15)int14)int10)int9)int8,(((144,(58,(43,68)int90)int86,(56,(44,47)int122)int87)int45,((64,65)int91,(66,(143,(54,(142,48)int92)int85)int59)int50)int40)int26,(45,((40,(12,52)int126)int69,(60,(134,(145,63)int129)int82)int60)int41)int36,(61,41,51,(46,(39,55)int89)int68,(50,62)int130)int39)int16)int7)int6)int5)int4)int3)int2,(7,8)int120)int1)int0)root;";
        correctnessTester.test("data/150-taxa-100-trees.tre", hashCS, 100);

        ///
        /// 150-taxa-1000-trees.tre

        hashCS = "(3,1,(2,(((((11,(((146,147)int88,(21,(19,(20,24)int89)int63)int54)int45,(18,(22,23)int114)int64)int30)int28,(14,((15,16)int87,(27,(10,25)int86)int77)int52)int46)int19,(17,26)int90)int16,((4,6)int111,(9,(5,((((49,((67,90)int101,(76,83,(118,(150,(127,(((112,82)int116,(71,93)int100)int62,((137,138,139)int71,((85,86)int98,(97,(120,((59,84)int99,(94,(88,98)int124)int81)int49)int44)int39)int33)int25)int20)int18)int17)int15,((87,(140,(99,((109,123)int117,(125,81)int97)int58,(141,(80,(100,96)int118)int70)int59)int31)int29)int26,(149,(126,148)int121)int69,(103,104)int96)int21)int11)int10,(117,(124,(95,(122,78)int95)int68)int57)int53)int9,(92,(57,((((102,110)int105,(106,79)int106)int61,(70,(119,121)int104)int73)int41,((115,91)int107,((130,42)int115,(89,(114,116,131)int74)int60)int43)int34)int22,(74,(128,73,((132,129,136)int72,(135,72)int120)int50)int40)int36,(108,(29,69)int108)int75,(133,53)int102,(105,113)int103)int14)int12)int8,((111,(101,77)int113)int76,((28,37,(31,(30,32)int123)int84)int47,((33,36)int110,(38,(34,35)int109)int79)int51)int27)int24,(107,75)int94)int7,(((144,(58,(43,68)int91)int80,(56,(44,47)int119)int83)int42,((64,65)int92,(66,(143,(54,(142,48)int112)int82)int56)int48)int38)int23,(41,61,51,(50,13,62)int66,(46,(39,55)int93)int67)int32,(45,((40,(12,52)int122)int65,(60,(134,(145,63)int125)int78)int55)int37)int35)int13)int6)int5)int4)int3)int2,(7,8)int85)int1)int0)root;";
        correctnessTester.test("data/150-taxa-1000-trees.tre", hashCS, 1000);

        ///
        /// 567-taxa-10-trees.tre

        hashCS = "(564,((((((112,(((((((((((((257,278,183,102,(231,((265,(317,539,(378,380)int404)int208)int173,(296,(104,((1,210)int494,(269,(421,451)int403)int276)int172)int148)int129)int88,((259,282)int401,(377,(290,4)int402)int275)int171)int63,(193,(76,98)int398)int273,(329,(267,322)int400)int274,(431,324,363)int277,(307,435)int501)int40,((25,490)int399,(46,((118,303)int395,(171,534)int396)int206)int170)int138,(200,(218,(169,318)int397)int272)int207)int32,(10,(((198,(161,53)int493)int279,(480,(147,(188,413)int409)int278)int210)int130,(373,45)int408)int106)int102,(((199,75,(255,410)int407)int209,(479,492)int406)int149,(302,379)int405)int116)int27,((((((439,(108,531)int392)int271,(48,(187,476)int393)int310)int158,(558,(((110,(258,530)int495)int270,(209,339)int390)int188,(404,64)int391)int128)int115)int78,(489,(289,(400,424)int394)int311)int205)int62,(((105,(18,61)int385)int267,(346,(101,285)int386)int268)int147,(207,(119,430)int384)int266)int105,((139,143)int389,((149,150)int388,(160,(519,55)int387)int269)int169)int127)int38,((417,472)int382,(500,(54,(16,407)int383)int265)int229)int146)int34,(235,295)int381)int19,(511,(((((12,(503,532)int373)int261,(131,(123,419)int374)int262)int144,(551,((151,294)int372,(3,8)int371)int203)int168)int92,(465,80)int375)int80,(355,(62,(342,(((232,521)int370,(524,(496,186,(364,(230,66)int369)int259)int167)int143)int114,(33,523)int368)int101)int94,(26,(247,455)int496)int260)int74)int68)int44,(((338,99)int379,(475,((293,52)int378,(436,(97,(126,73)int377)int263)int204)int145,(212,305)int376)int104)int93,(538,(13,74)int380)int264)int77)int31,((((124,190)int365,(314,427)int364)int202,(432,514)int363)int157,((252,429)int362,(340,371)int361)int201)int96,((383,(221,336)int360)int257,(550,(59,(213,233)int359)int256)int200)int126,((280,29)int367,(493,(148,491)int366)int258)int166)int16,(301,549)int358)int15,(158,(((113,(375,((146,(288,(185,473)int355)int253)int198,(516,(387,(241,347)int354)int252)int197)int112,(443,(((253,(462,502)int502)int254,(385,461)int497)int165,(279,426)int356)int125)int113)int67)int65,(20,308)int353)int57,(206,(179,(142,242)int357)int255)int199)int52)int48)int12,(((((141,15)int452,(((208,((100,396)int449,(260,(163,264)int450)int295)int184)int153,(390,(196,84)int451)int296)int108,(362,94)int448)int89)int81,(((319,504,177,91,((128,156)int444,(457,(446,(6,(34,35)int445)int293)int219)int180)int134,(315,(125,(24,(22,36)int443)int292)int218)int179,(271,(132,(223,398)int441)int290)int217,(351,(145,237)int442)int291,(216,(240,501)int447)int294,(203,515)int446)int45,(274,(323,(384,520)int440)int313)int216)int42,((((229,277)int439,(402,(251,272)int438)int289)int178,(60,((((((165,30)int434,((243,376)int435,(408,483)int436)int235)int152,(234,325)int506)int118,((452,547)int432,(542,(2,(505,(178,463)int433)int288)int215)int177)int133)int76,(192,194)int437)int66,(((180,499)int431,(((328,361)int428,((464,67)int430,(51,(222,(529,(248,507)int429)int286)int214)int176)int132)int107,(394,(140,19)int491)int287)int86)int79,(448,(311,(134,92)int427)int285)int234)int64)int36)int35)int33,((268,(541,((345,204,106,93,423,546,89,27,82,((88,(127,298)int492)int283,(382,401)int420,(381,77)int421)int131,((405,540)int422,(474,(522,(287,50)int423)int284)int212)int151,(526,(441,(157,498)int425)int309)int213,(471,544)int419,(299,545)int424,(173,411)int426)int41,(284,365)int418)int37,(((((137,275)int413,((184,467)int504,(392,(356,(313,357)int500)int281)int211)int150)int117,(262,337)int414)int97,(263,68)int412)int85,((168,((111,445)int416,(333,386)int415)int228)int174,(487,(220,(205,(31,354)int417)int282)int227)int175)int98)int54)int29)int28,(367,(197,(219,43)int411)int280)int232)int26)int20)int18)int17,((174,518)int464,(438,((((133,418)int456,(306,415)int455)int220,((182,353)int457,(23,536)int458)int221,(214,509)int459)int99,(477,(42,((((191,(166,403)int462)int299,(332,70)int463)int182,(334,(389,422)int461)int316)int119,(494,(109,488)int460)int298)int90)int87)int82)int53)int51)int47,(468,(368,((167,548)int454,(195,(372,453)int453)int297)int181)int154)int135)int14,(58,9)int410)int13)int11,(236,349)int465)int10,(172,(374,81)int466)int300,(517,537)int467)int9,(326,450)int352)int8,(352,(412,(409,447)int468)int301)int222)int7,((202,((((107,350)int475,((225,261)int474,(433,(138,552)int473)int304)int183)int136,(327,525)int472)int110,((122,291)int470,(456,(162,(14,478)int471)int303)int223)int155)int75)int69,(425,(170,266)int469)int302)int60)int6)int5,(((((120,95)int477,(508,(512,(181,57)int476)int305)int224)int156,((((164,217)int479,(309,(316,320)int478)int306)int189,(201,(28,40)int480)int307)int120,(292,348)int481)int100)int70,(((228,300)int482,(239,((121,459)int483,(244,393)int484)int230)int185)int137,(270,90)int485)int109)int49,(((256,460)int489,(388,406)int488)int226,((297,38)int487,(39,458)int486)int225)int121)int39)int4,(246,(116,454)int351)int251)int3,(5,((((((((((((144,(510,(175,335)int334)int314)int193,(321,86)int335)int159,(444,(226,(245,559)int505)int312)int194)int103,(369,(434,(395,497)int336)int246)int195,(249,96)int499)int73,(343,(189,344)int337)int315)int59,(159,87)int333)int56,((330,85)int338,(399,(276,416)int339,(103,114)int340)int186)int123)int46,(((250,397)int331,(420,528)int332)int192,(484,((227,(428,7)int330)int245,((286,(154,442)int329)int244,(556,(437,(211,(370,557)int328)int243)int191)int162)int111)int91)int84)int72)int30,(69,(((224,37)int322,(554,(((((11,(117,56)int324)int240,(341,(469,71)int325)int241)int142,(41,312,(358,(310,136,(449,481)int326)int231)int161)int139)int83,(17,(130,254)int327)int242)int71,(553,78)int323)int61)int58)int55,(281,(152,513)int321)int239)int50,(440,(331,63)int498)int238,(155,366)int320)int43)int25,(((115,(535,555)int344)int247,(482,((129,304)int342,(470,533)int341)int196)int163)int122,(135,65)int343)int95,(47,(495,(215,(153,486)int346)int249)int233)int164,(79,(176,506)int345)int248)int24,(283,391)int347)int23,((238,485)int348,((32,(543,560)int349)int250,(414,527)int350)int187)int124)int22)int21)int2,(44,(273,466)int490)int308)int1,(21,(49,((359,360)int503,(72,83)int319)int190)int160)int141)int0,((561,(562,563)int317)int236,(565,(566,567)int318)int237)int140)root;";
        correctnessTester.test("data/567-taxa-10-trees.tre", hashCS, 10);

        ///
        /// 567-taxa-1000-trees.tre

        hashCS = "(564,((((112,(((((((((((((183,257,(231,((265,(317,539,(378,380)int369)int206)int175,(296,(104,((1,210)int498,(269,(421,451)int370)int270)int176)int150)int131)int92,((259,282)int368,(377,(290,4)int497)int269)int174)int63,((25,490)int364,(46,((118,303)int365,(171,534)int366)int205)int173)int142,((102,(278,(307,435)int502)int325)int241,(193,(76,98)int367)int268)int143,(200,(218,(169,318)int371)int271)int240,(363,324,431)int267,(329,(267,322)int512)int319)int33,(10,(((198,(161,53)int507)int266,(480,(147,(188,413)int363)int265)int204)int130,(373,45)int362)int109)int107,(((199,75,(255,410)int374)int207,(479,492)int373)int151,(302,379)int372)int117)int26,((((((105,(18,61)int378)int273,(346,(101,285)int379)int274)int152,(207,(119,430)int380)int275)int110,((139,143)int376,((149,150)int377,(160,(519,55)int501)int272)int189)int132)int75,((((439,(108,531)int382)int328,(48,(187,476)int383)int318)int163,(558,(((110,(258,530)int493)int276,(209,339)int385)int194,(404,64)int384)int133)int118)int81,(489,(289,(400,424)int381)int317)int208)int64)int39,((417,472)int387,(500,(54,(16,407)int386)int277)int209)int153)int35,(235,295)int375)int18,(((((((12,(503,532)int356)int259,(131,(123,419)int357)int260)int148,(551,((151,294)int354,(3,8)int355)int203)int171)int95,(465,80)int353)int84,(355,((342,(((232,521)int492,(524,(186,496,(364,(230,66)int358)int262)int172)int149)int116,(33,523)int359)int106)int98,(62,(26,(247,455)int500)int261)int246)int78)int70)int44,(511,(((338,99)int349,(475,((293,52)int505,(436,(97,(126,73)int351)int258)int202)int147,(212,305)int352)int108)int96,(538,(13,74)int350)int257)int80)int79)int31,((280,29)int499,(493,(148,491)int348)int256)int170)int30,((383,(221,336)int361)int264,(550,(59,(213,233)int360)int263)int235)int129)int15,((((124,190)int390,(314,427)int389)int210,(432,514)int388)int162,((252,429)int391,(340,371)int392)int211)int99)int14,(301,549)int347)int13,(158,((113,(375,(((146,(288,(185,473)int345)int255)int201,(516,(387,(241,347)int344)int254)int200)int126,(443,(((253,(462,502)int510)int321,(385,461)int504)int169,(279,426)int343)int141)int115)int76)int69)int67,(20,308)int346)int58,(206,(179,(142,242)int342)int253)int233)int48)int10,(((((141,15)int438,(((208,((100,396)int435,(260,(163,264)int513)int292)int192)int156,(390,(196,84)int436)int293)int112,(362,94)int437)int94)int86,(((((229,277)int425,(402,(251,272)int506)int288)int181,(60,((((((165,30)int494,(243,376)int428)int243,(408,483)int429)int155,(234,325)int520)int121,((452,547)int426,(542,(2,(505,(178,463)int427)int311)int232)int182)int135,(192,194)int509)int68,(((180,499)int503,(((328,361)int431,((464,67)int433,(51,(222,(529,(248,507)int432)int290)int220)int183)int136)int111,(394,(140,19)int430)int289)int89)int82,(448,(311,(134,92)int434)int291)int245)int65)int37)int36)int34,((268,(541,((((((137,275)int422,((184,467)int518,(392,(356,(313,357)int515)int287)int219)int154)int120,(262,337)int423)int102,(263,68)int424)int91,((168,((111,445)int420,(333,386)int419)int218)int179,(487,(220,(205,(31,354)int421)int315)int238)int180)int101)int55,((284,365)int418,(82,(((157,498)int417,(441,526)int519)int217,((204,((88,(127,298)int511)int285,(381,77)int412,(382,401)int413)int134,((405,540)int414,(474,(522,(287,50)int415)int286)int231)int161,(423,((106,93)int516,(299,545)int416)int242)int196)int62,(345,(471,544)int411)int327)int56)int47,((27,89)int517,(546,(173,411)int410)int323)int197)int42)int38)int29)int28)int27,(367,(197,(219,43)int495)int310)int237)int25)int19,((274,(323,(384,520)int409)int326)int216,((315,(125,(24,(22,36)int402)int280)int214)int178,(446,319,(91,504,177,(457,(6,(34,35)int407)int284,(128,156)int408)int166,(216,(240,501)int405)int283,(203,515)int406)int83,(271,(132,(223,398)int403)int281)int215,(351,(145,237)int404)int282)int54)int45)int41)int17)int16,((174,518)int393,(438,(((((133,418)int400,(306,415)int399)int213,((182,353)int398,(23,536)int397)int212)int127,(214,509)int401)int100,(477,(42,((334,(((191,(166,403)int496)int278,(332,70)int395)int177,(389,422)int394)int144)int119,(494,(109,488)int396)int279)int93)int88)int85)int52)int50)int46,(468,(368,((167,548)int440,(195,(372,453)int439)int294)int184)int157)int137)int12,(58,9)int441)int11)int9,(236,349)int442)int8,(172,(374,81)int341)int252,(517,537)int340)int7,(326,450)int339)int6,(352,(412,(409,447)int443)int295)int221)int5,((202,((((107,350)int332,((225,261)int333,(433,(138,552)int334)int249)int168)int128,(327,525)int335)int114,((122,291)int337,(456,(162,(14,478)int336)int250)int199)int165)int77)int71,(425,(170,266)int338)int251)int59)int4)int3,(5,(((((((((((144,(510,(175,335)int479)int320)int228,(321,86)int480)int164,(444,(226,(245,559)int521)int324)int229)int105,(369,(434,(395,497)int478)int304)int227,(249,96)int522)int74,(343,(189,344)int481)int322)int61,((250,397)int476,(420,528)int477)int226)int53,(484,((227,(428,7)int483)int305,((286,(154,442)int485)int306,(556,(437,(211,(370,557)int484)int316)int230)int195)int124)int97)int90,((330,85)int473,(399,(103,114)int474,(276,416)int475)int198)int139,(159,87)int482)int32,(69,(((224,37)int461,(554,(((((11,(117,56)int465)int301,(341,(469,71)int464)int312)int159,(312,(41,(358,(136,(310,(449,481)int463)int329)int236)int186)int167)int145)int87,(17,(130,254)int462)int300)int72,(553,78)int466)int66)int60)int57,(281,(152,513)int467)int313)int51,(440,(331,63)int508)int314,(155,366)int460)int43)int24,(((115,(535,555)int471)int302,(482,((129,304)int469,(470,533)int468)int225)int187)int125,(135,65)int470)int104,(47,(495,(215,(153,486)int459)int299)int239)int185,(79,(176,506)int472)int303)int23,(283,391)int458)int22,((238,485)int486,((32,(543,560)int488)int307,(414,527)int487)int190)int140)int21)int20,(((((120,95)int457,(508,(512,(181,57)int456)int298)int224)int158,((((164,217)int455,(309,(316,320)int454)int297)int193,(201,(28,40)int453)int296)int123,(292,348)int452)int103)int73,(((228,300)int450,(239,((121,459)int448,(244,393)int449)int244)int191)int138,(270,90)int451)int113)int49,(((256,460)int447,(388,406)int446)int223,((297,38)int444,(39,458)int445)int222)int122)int40,(246,(116,454)int489)int308)int2,(44,(273,466)int490)int309)int1,(21,(49,((359,360)int514,(72,83)int491)int234)int188)int160)int0,((561,(562,563)int331)int248,(565,(566,567)int330)int247)int146)root;";
        correctnessTester.test("data/567-taxa-100-trees.tre", hashCS, 100);

        ///
        /// 567-taxa-1000-trees.tre

        hashCS = "(564,(((((112,(((((((((((((183,257,(231,((265,(539,(317,(378,380)int421)int329)int217)int179,(296,(104,((1,210)int512,(269,(421,451)int422)int310)int180)int155)int140)int93,((259,282)int423,(377,(290,4)int495)int278)int181)int64,((25,490)int427,(46,((118,303)int425,(171,534)int426)int218)int182)int143,((102,(278,(307,435)int505)int325)int240,(193,(76,98)int420)int277)int144,(200,(218,(169,318)int424)int279)int244,(324,431,363)int276,(329,(267,322)int513)int319)int34,(10,(((198,(161,53)int506)int281,(480,(147,(188,413)int431)int280)int220)int141,(373,45)int430)int113)int108,(((75,199,(255,410)int429)int219,(479,492)int487)int156,(302,379)int428)int123)int26,((((((105,(18,61)int441)int285,(346,(101,285)int442)int286)int158,(207,(119,430)int443)int287)int114,((139,143)int439,((149,150)int440,(160,(519,55)int503)int284)int190)int136)int76,((((439,(108,531)int436)int324,(48,(187,476)int435)int318)int164,(558,(((110,(258,530)int490)int283,(209,339)int438)int194,(404,64)int437)int135)int124)int83,(489,(289,(400,424)int434)int320)int221)int65)int40,((417,472)int432,(500,(54,(16,407)int433)int282)int235)int157)int36,(235,295)int419)int19,(((((((12,(503,532)int411)int312,(131,(123,419)int410)int271)int152,(551,((151,294)int412,(3,8)int413)int230)int178)int97,(465,80)int409)int87,(355,((342,(((232,521)int485,(524,(186,496,(364,(230,66)int480)int272)int189)int153)int122,(33,523)int414)int107)int98,(62,(26,(247,455)int501)int273)int245)int79)int71)int45,(511,(((338,99)int418,(475,((293,52)int496,(436,(97,(126,73)int417)int275)int216)int154,(212,305)int416)int112)int96,(538,(13,74)int415)int274)int82)int80)int32,((280,29)int494,(493,(148,491)int444)int288)int183)int30,((383,(221,336)int445)int289,(550,(59,(213,233)int446)int290)int233)int137)int16,((((124,190)int407,(314,427)int406)int214,(432,514)int405)int163,((252,429)int484,(340,371)int408)int215)int104)int15,(301,549)int404)int14,(158,((113,(375,(((146,(288,(185,473)int401)int269)int213,(516,(387,(241,347)int400)int268)int212)int126,(443,(((253,(462,502)int511)int317,(385,461)int507)int177,(279,426)int399)int142)int121)int77)int69)int67,(20,308)int402)int58,(206,(179,(142,242)int403)int270)int232)int49)int11,(((((141,15)int365,(((208,((100,396)int363,(260,(163,264)int514)int309)int192)int160,(390,(196,84)int479)int257)int110,(362,94)int364)int95)int86,(((((229,277)int389,(402,(251,272)int509)int265)int174,(60,((((((165,30)int483,(243,376)int397)int241,(408,483)int396)int151,(234,325)int522)int120,((452,547)int394,(542,(2,(505,(178,463)int395)int307)int231)int176)int134,(192,194)int508)int68,(448,(((180,499)int502,(((328,361)int390,((464,67)int391,(51,(222,(529,(248,507)int392)int266)int228)int175)int133)int111,(394,(140,19)int500)int308)int90)int81,(311,(134,92)int393)int267)int70)int63)int38)int37)int35,((268,(541,((((((137,275)int376,((184,467)int519,(392,(356,(313,357)int515)int262)int209)int150)int119,(262,337)int375)int102,(263,68)int374)int92,((168,((111,445)int377,(333,386)int378)int234)int173,(487,(220,(205,(31,354)int379)int315)int239)int188)int103)int56,((284,365)int388,(82,((204,(((381,77)int385,(382,401)int386)int246,(88,(127,298)int504)int264)int132,((405,540)int383,(474,(522,(287,50)int382)int263)int211)int162,(423,((106,93)int516,(299,545)int384)int243)int197)int62,((157,498)int380,(441,526)int523)int210,(345,(471,544)int381)int326)int48,((27,89)int517,(546,(173,411)int387)int323)int196)int43)int39)int29)int28)int27,(367,(197,(219,43)int491)int311)int238)int25)int20,((274,(323,(384,520)int373)int328)int208,((315,(125,(24,(22,36)int372)int261)int207)int172,(446,319,(504,91,177,(457,(6,(34,35)int369)int303,(128,156)int368)int167,(216,(240,501)int366)int258,(203,515)int367)int84,(271,(132,(223,398)int370)int259)int206,(351,(145,237)int371)int260)int55)int46)int42)int18)int17,((174,518)int354,(438,(((((133,418)int361,(306,415)int360)int205,((182,353)int359,(23,536)int358)int229)int128,(214,509)int362)int101,(477,(42,((334,(((191,(166,403)int498)int255,(332,70)int355)int171,(389,422)int356)int145)int118,(494,(109,488)int357)int256)int94)int89)int85)int53)int51)int47,(468,(368,((167,548)int353,(195,(372,453)int493)int254)int170)int149)int131)int13,(58,9)int398)int12)int10,(236,349)int352)int9,(172,(374,81)int447)int291,(517,537)int351)int8,(326,450)int448)int7,(352,(412,(409,447)int350)int253)int204)int6,((202,((((107,350)int348,((225,261)int486,(433,(138,552)int349)int252)int169)int130,(327,525)int347)int115,((122,291)int346,(456,(162,(14,478)int482)int251)int203)int165)int78)int73,(425,(170,266)int345)int250)int59)int5)int4,(((((120,95)int344,(508,(512,(181,57)int489)int305)int202)int148,((((164,217)int342,(309,(316,320)int343)int249)int198,(201,(28,40)int341)int304)int117,(292,348)int340)int100)int72,(((228,300)int336,(239,(244,393)int337,(121,459)int338)int193)int129,(270,90)int339)int109)int50,(((256,460)int335,(388,406)int334)int201,((297,38)int497,(39,458)int333)int200)int116)int41,(246,(116,454)int449)int292)int3,(5,(((((((((((144,(510,(175,335)int471)int322)int227,(321,86)int472)int166,(444,(226,(245,559)int521)int327)int226)int106,(369,(434,(395,497)int481)int300)int225,(249,96)int520)int75,(343,(189,344)int473)int321)int61,((250,397)int470,(420,528)int499)int224)int54,(484,((227,(428,7)int467)int298,((286,(154,442)int469)int299,(556,(437,(211,(370,557)int468)int314)int223)int195)int125)int99)int91,((330,85)int465,(399,(103,114)int464,(276,416)int466)int199)int138,(159,87)int474)int33,(79,(176,506)int463)int297)int31,(69,(((224,37)int456,(554,(((((11,(117,56)int459)int295,(341,(469,71)int458)int306)int161,(312,(41,(358,(136,(310,(449,481)int488)int330)int236)int186)int168)int146)int88,(17,(130,254)int460)int296)int74,(553,78)int457)int66)int60)int57,(281,(152,513)int461)int313)int52,(440,(331,63)int510)int316,(155,366)int462)int44,(((115,(535,555)int452)int294,(482,((129,304)int454,(470,533)int453)int222)int185)int127,(135,65)int455)int105,(47,(495,(215,(153,486)int451)int293)int242)int184)int24,(283,391)int450)int23,((238,485)int475,((32,(543,560)int492)int301,(414,527)int476)int191)int139)int22)int21)int2,(44,(273,466)int477)int302)int1,(21,(49,((359,360)int518,(72,83)int478)int237)int187)int159)int0,((561,(562,563)int332)int248,(565,(566,567)int331)int247)int147)root;";
        correctnessTester.test("data/567-taxa-1000-trees.tre", hashCS, 1000);

    }

    public void test(String fileName, String hashCS, int noOfTrees) {
        System.out.printf("> Testing: '%s' (%d trees)\n", fileName, noOfTrees);
        CTMain main = new CTMain();
        String[] trees = null;
        try {

            trees = CTMain.readTreesFromFile(prefix + "" + fileName, noOfTrees);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        NewickParser parser = new NewickParser(trees[0]);
        TreeNode first = parser.parse();
        main.initialize(first, noOfTrees);
        main.addNewTree(first);
        CTree tree = null;
        for (int i = 1; i < noOfTrees; i++) {
            NewickParser parser2 = new NewickParser(trees[i]);
            main.addNewTree(parser2.parse());
//            main.printConfig();
            tree = main.constructMajorityTree();
        }
        System.out.println(tree.getRoot().toString());
        if (testTrees(tree.toString(), hashCS)) {
            System.out.println("All was well. Thank god.");
        } else {
            System.out.println("> ERROR. Something went wrong!");
            System.exit(-1);
        }
    }

    public boolean testTrees(String ours, String hashCS) {
        //System.out.println(s);
        //System.out.println(hashCS);

        NewickParser parser = new NewickParser(ours);
        TreeNode tree = parser.parse();

        NewickParser parser2 = new NewickParser(hashCS);
        TreeNode tree2 = parser2.parse();

        markDepth(tree, 0);
        markDepth(tree2, 0);

        List<TreeNode> leaves = tree.getLeaves();
        List<TreeNode> leaves2 = tree.getLeaves();

        for (TreeNode leaf : leaves) {
            boolean found = false;
            for (TreeNode other : leaves2) {
                if (leaf.name.equals(other.name)) {
                    int leafDepth = leaf.getIntProperty("depth");
                    int otherDepth = leaf.getIntProperty("depth");
                    if (leafDepth != otherDepth) return false;
                    found = true;
                }
            }
            if (!found) return false;
        }

        return true;
    }

    public void markDepth(TreeNode node, int depth) {
        if (node.isLeaf()) {
            node.addProperty("depth", depth);
        }
        for (int i = 0; i < node.children.size(); i++) {
            markDepth(node.children.get(i), depth + 1);
        }
    }

}
1、这是一款辅助java代码审计的小工具。通过匹配指定目录下所有文件中包含的java的一些危险函数，可能误报高，但对于审计多少有些帮助！  


2、使用者只需要自定义关键词路径、输出结果的位置、搜索的项目路径即可。
![image](https://github.com/user-attachments/assets/d2d6fa12-f2dc-40ec-a8a2-2442b1eebb53)


3、在这个小项目中keywords.txt就是关键词路径，这是我平时收集到的。使用者可以自己丰富这个文件(注意:新增漏洞类型的话，需要一行一个;危险函数之间英文逗号隔开，逗号和函数之间不能有空格)
比如:vul:xxx,xx,xx,xxx
![image](https://github.com/user-attachments/assets/d240f846-5318-4dba-a586-13cf96258021)  


4、运行java文件后，会在你指定的路径下生成结果文件(需要提前创建好该文件)。在结果文件中会输出文件路径、总行数、漏洞类型、匹配的关键字、具体行的代码，很清晰
![image](https://github.com/user-attachments/assets/da1293fc-a7c2-461b-b92c-5bb900c13d8a)


小白初学,参考GPT,轻喷;参考大佬的项目(该项目好像只能用在python2环境):https://github.com/Cryin/JavaID



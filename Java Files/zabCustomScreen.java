package zaberp.zab;

import java.sql.*;
import java.util.*;
import java.io.*;

public class zabCustomScreen {

  static final char DQUOTE = '\"';
  static final String SEPvalue = "" + '\u00FF';
  static String semester = "";
  static String semestername = "";
  static String studentid = "";
  public static String message = "";
  static int totalcount = 0;
  static String id = "";
  static java.sql.Date schdate = new java.sql.Date(System.currentTimeMillis());
  static String sql = "";
  static zabInfo smdata = new zabInfo();
  static String [] result = null;
  static String [] datarow = null;
  static String [] sa = null;
  static double totalfees=0;
  static double tutionfees = 0;
  static Hashtable totFees = new Hashtable();
  static double firstinst = 0;
  static double secinst = 0;
  static double thirdinst = 0;

  static String fname = "";//+zabinfo.getLookupValue("smstudent.xfirst(xstudentid = '"+studentid+"')");
  static String mname = "";//+zabinfo.getLookupValue("smstudent.xmiddle(xstudentid = '"+studentid+"')");
  static String lname = "";//+zabinfo.getLookupValue("smstudent.xlast(xstudentid = '"+studentid+"')");
  static String department = "";//+zabinfo.getLookupValue("xcodes.xlong(xtype='Department' and xcode = '"+""+zabinfo.getLookupValue("smstudent.xdepartment(xstudentid = '"+studentid+"')")+"')");
  static String cgpa = "";
  static String addsemester = "";
//  static String []totFees = new String[100];

  public zabCustomScreen() {
  }

  public static String specialPage(zabInfo zabinfo,String screenName){
	  System.err.println(screenName);
    /*
	  semester = ""+zabinfo.ses.getAttribute("semester");
    studentid = ""+zabinfo.ses.getAttribute("email");
    semestername = ""+zabinfo.getLookupValue("xcodes.xlong(xtype='Semester' and xcode = '"+semester+"')");
    String sql = "";
//System.err.println(studentid);
    fname = ""+zabinfo.getLookupValue("smstudent.xfirst(xstudentid = '"+studentid+"')");
    mname = ""+zabinfo.getLookupValue("smstudent.xmiddle(xstudentid = '"+studentid+"')");
    lname = ""+zabinfo.getLookupValue("smstudent.xlast(xstudentid = '"+studentid+"')");
    department = ""+zabinfo.getLookupValue("xcodes.xlong(xtype='Department' and xcode = '"+""+zabinfo.getLookupValue("smstudent.xdepartment(xstudentid = '"+studentid+"')")+"')");
    cgpa = ""+zabinfo.getLookupValue("smstudent.xcgpa(xstudentid = '"+studentid+"')");
    addsemester = ""+zabinfo.getLookupValue("smstudent.xsemester(xstudentid = '"+studentid+"')");
*/
    String command = zabinfo.getParameter("command");
    if (command == null) command = "";
    //if(command.equals("))
    
    if(command.equals("Next")){
      smdata.validclasses.clear();
      for(int i =0;i<totalcount;i++){
        String selectedclass = zabinfo.getParameter("xselectcourse"+i+"");
        if((selectedclass == null) || (selectedclass.equals("null"))) selectedclass = "";
        if(!selectedclass.equals("")){
          smdata.validclasses.put(selectedclass,selectedclass);
//          sql = "select xcount,xcapacity,xcode from smclassheader where xclassid = '"+selectedclass+"'";
//          result = zabinfo.getSqlRow(sql);
//          sql = "update smclassheader set xcount="+(Tools.getInt(result[0])+1)+"";
//          if((Tools.getInt(result[0])+1)==Tools.getInt(result[1]))
//            sql += ",xstatus='Full'";
//          sql += " where xclassid='"+selectedclass+"'";
//          zabinfo.executeSql(sql);
        }
      }
      return Registration(smdata);
    }else if(command.equals("Submit Registration")){

      // UPDATE CLASS CAPACITY

      for(int i =0;i<totalcount;i++){
        String selectedclass = zabinfo.getParameter("xselectcourse"+i+"");
        if((selectedclass == null) || (selectedclass.equals("null"))) selectedclass = "";
        if(!selectedclass.equals("")){
          sql = "select xcount,xcapacity,xcode from smclassheader where xclassid = '"+selectedclass+"'";
          result = zabinfo.getSqlRow(sql);
          sql = "update smclassheader set xcount="+(zabTools.getInt(result[0])+1)+"";
          if((zabTools.getInt(result[0])+1)==zabTools.getInt(result[1]))
            sql += ",xstatus='Full'";
          sql += " where xclassid='"+selectedclass+"'";
          zabinfo.executeSql(sql);
        }
      }

      //

      int row = 10;
      sql = "insert into smstudentsemester(zid,xstudentid,xsemester) values("+smdata.ses.getAttribute("id")+",'"+studentid+"','"+semester+"')";
      zabinfo.executeSql(sql);
      Enumeration en = totFees.keys();
      sql = "select xrow from smfees";
      result = zabinfo.getSqlValues(sql);
      for(int i =0;i<result.length;i++){
        sql = "select xtype,xitem,xamount,xsign,xletter from smstudentfees where xrow="+zabTools.getInt(result[i])+"";
        datarow=zabinfo.getSqlRow(sql);
        en = totFees.keys();
        while(en.hasMoreElements()){
          Object o = en.nextElement();
          if(datarow[1].equals(o)){
//System.err.println(datarow[i]);
            sql = "insert into smstudentfeesdetail(zid,xrow,xstudentid,xsemester,xitem,xamount,xletter,xsign,xtype)";
            sql += " values("+smdata.ses.getAttribute("id")+","+row+",'"+studentid+"','"+semester+"','"+datarow[1]+"',"+zabTools.getDouble(""+totFees.get(o))+",'Addition',"+zabTools.getInt(datarow[3])+",'"+datarow[0]+"')";
            smdata.executeSql(sql);
          //System.out.println(datarow[1]+" : "+totFees.get(o));
            row = row+10;
          }
        }
      }
      en = smdata.validclasses.keys();
      String coursecode = "";
      while(en.hasMoreElements()){
        Object o = en.nextElement();
        coursecode = ""+zabinfo.getLookupValue("smclassheader.xcode(xclassid = '"+o+"')");
        sql = "update smstudentcurriculum set xsemester='"+semester+"'";
        sql += ",xcoursecount='Yes',xclassid='"+o+"'";
        sql += " where xcode='"+coursecode+"' and xcoursecount='No' and xstudentid='"+studentid+"'";
        sql = smdata.executeSql(sql);

      }


      return Registration(smdata);
    }else if(command.equals("Back")){
      return Search(smdata);
    }else if(command.equals("Ok")){
      insertIntoAR(smdata);

/*      int row = 10;
      sql = "insert into smstudentsemester(zid,xstudentid,xsemester) values("+smdata.ses.getAttribute("id")+",'"+studentid+"','"+semester+"')";
      zabinfo.executeSql(sql);
      Enumeration en = totFees.keys();
      sql = "select xrow from smfees";
      result = zabinfo.getSqlValues(sql);
      for(int i =0;i<result.length;i++){
        sql = "select xtype,xitem,xamount,xsign from smfees where xrow="+zabTools.getInt(result[i])+"";
        datarow=zabinfo.getSqlRow(sql);
        en = totFees.keys();
        while(en.hasMoreElements()){
          Object o = en.nextElement();
          if(datarow[1].equals(o)){
            sql = "insert into smstudentfeesdetail(zid,xrow,xstudentid,xsemester,xitem,xamount,xtype,xsign) values("+smdata.ses.getAttribute("id")+","+row+",'"+studentid+"','"+semester+"','"+datarow[1]+"',"+zabTools.getDouble(""+totFees.get(o))+",'Addition',"+zabTools.getInt(datarow[3])+")";
            smdata.executeSql(sql);
          //System.out.println(datarow[1]+" : "+totFees.get(o));
            row = row+10;
          }
        }
      }
      en = smdata.validclasses.keys();
      String coursecode = "";
      while(en.hasMoreElements()){
        Object o = en.nextElement();
        coursecode = ""+zabinfo.getLookupValue("smclassheader.xcode(xclassid = '"+o+"')");
        sql = "update smstudentcurriculum set xsemester='"+semester+"'";
        sql += ",xcoursecount='Yes',xclassid='"+o+"'";
        sql += " where xcode='"+coursecode+"' and xcoursecount='No' and xstudentid='"+studentid+"'";
        sql = smdata.executeSql(sql);

      }
    */
      return StudentRegistration(smdata);
    }else if(command.equals("<Yesterday")){
      schdate = new java.sql.Date(schdate.getTime()-(24*60*60*1000));
      //ScheduleMain(smdata,schdate);
    }else if(command.equals("Tomorrow>")){
      schdate = new java.sql.Date(schdate.getTime()+(24*60*60*1000));
      //ScheduleMain(smdata,schdate);
    }


//      semester = (String)zabinfo.ses.getAttribute("regsemester");
      id = (String)zabinfo.ses.getAttribute("id");
    String screen = (String)zabinfo.globals.get("screen");
    if (screen.startsWith("*smstudentprereg")){
      return Search(zabinfo);
    }else if (screen.startsWith("*smstudentreg")){
      return StudentRegistration(zabinfo);
    }else if (screen.startsWith("*smgradereportbysemester")){
      return GradeReportBySemester(zabinfo);
    }else if (screen.startsWith("*smgradereportbycurriculum")){
      return GradeReportByCurriculum(zabinfo);
    }else if (screen.startsWith("*smstudentschedule")){
      return StudentSchedule(zabinfo);
    }else if (screen.startsWith("*smstudentcurriculum")){
      return StudentCurriculum(zabinfo);
    }else if (screen.startsWith("*smstudentmain")){
      return StudentMain(smdata);
    }else{
      return "";
    }
 }

  public static String SearchOld(zabInfo zabinfo){
    String s = "";
    String sql = "";
    String serial1 ="";
    String person1 = "";
    String room1 = "";
    String floor1 = "";
    String wing1 = "";
    String sup1 = "";
    String type1 = "";
    String manufacturer1 = "";
    String ipadd1 = "";
    String macadd1 = "";
    String [] donecourses = null;
    String [] notdone = null;
    String [] prereq = null;
    String [] validcourse = new String[1000];
//    String [] validcode = null;
    int count = 0;

    //GETTING DONE COURSES
//    zabinfo.ses.setAttribute("studentid",studentid);
//    zabinfo.ses.setAttribute("semester",semester);
    sql = "select xcode from smstudentcurriculum where xcoursecount='Yes' and xstudentid='"+studentid+"'";
    donecourses = zabinfo.getSqlValues(sql);

    //GETTING UNDONE COURSES
    sql = "select xcode from smstudentcurriculum where xcoursecount='No' and xstudentid='"+studentid+"'";
    notdone = zabinfo.getSqlValues(sql);
    String check = "0";
    if(notdone == null) return "";

    for(int i = 0; i < notdone.length;i++){
      check="0";

      //GETTING PREREQ OF EACH UNDONE COURSES

      sql = "select xprereq from smstudentcurriculum where xcode='"+notdone[i]+"' and xstudentid='"+studentid+"'";
      datarow = zabinfo.getSqlRow(sql);
      prereq = zabTools.parse(datarow[0],",");

      if(donecourses == null){
        if(prereq.length==0){
          check = "1";

//          continue;
        }

      }

//      if(prereq.length>0){
      for(int j = 0; j < prereq.length;j++){

        //CHECKING WITH DONE COURSES FOR VALID COURSES
        if(donecourses == null){
          if(prereq[j].equals("Nil") || prereq[j].equals("")){
            check = "1";
            break;
          }
        }else if(donecourses != null){

          for(int z = 0; z < donecourses.length;z++){
            if(prereq.length==1){
              if((prereq[j].equals(donecourses[z])) || (prereq[j].equals("Nil"))){
                check="1";
                break;
              }
            }else if(prereq.length>1){
              if(!(prereq[j].equals(donecourses[z]))){
                check = "0";
                //break;
              }else{
                check = "1";
                break;
              }
            }
          }
        }
        if(prereq.length==1 && check.equals("1")) break;
        if(prereq.length>1 && check.equals("0")) break;
      }
//      }
      //PUTING VALID COURSES
      if(check.equals("1")){
        validcourse[count]=notdone[i];
        count=count+1;
//System.out.println(" atcount"+count);
      }
    }
    String days = "";

//    s = semester;
//    s += "<DIV STYLE=\"POSITION:RELATIVE;TOP:50;backgound:white\">";
    s += "<FORM onSubmit=\"return submitIt(this)\" ACTION=\""+zabinfo.servlet+"\" METHOD=POST name=\"login\">\n";
    s += "  <INPUT class=p NAME=screen TYPE=hidden VALUE=\"*smstudentprereg\">\n";

    s += "<TABLE BORDER=0 WIDTH=100% ><TR><TD>";
    s += "<SPAN STYLE=\"POSITION:RELATIVE;LEFT:0;FONT-FAMILY: Arial,Verdana, sans-serif; FONT-WEIGHT:BOLD; FONT-SIZE: 0.8em;\">Semester: "+semester+"  "+semestername+"</SPAN>";
    s += "</TD></TR></TABLE>";
//    s += "<TABLE WIDTH=100%><TR><TD>";
//    s += "<TD class=regth style=\"POSITION:RELATIVE;WIDTH:180;\">Course</TD><TD class=regth style=\"POSITION:RELATIVE;WIDTH:50;\">Check</TD><TD class=regth>Section</TD><TD class=regth>Day</TD><TD class=regth>Class Type</TD><TD class=regth>Room</TD><TD class=regth>Time</TD>";
//    s += "<TD class=regth>Day</TD><TD class=regth>Class Type</TD><TD class=regth>Room</TD><TD class=regth>Time</TD>";
//    s += "</TD></TR></TABLE>";
//    s += "<TABLE BORDER=10 WIDTH=100%>";
//    s += "  <TR><TD WIDTH=20%>Course</TD><TD>Check</TD><TD>Section</TD></TR>";
//    s += "<TR><TD colspan=2>";
    s += "<TABLE cellSpacing=0 cellPadding=0 BORDER=0 WIDTH=100%";
    s += " style=\"POSITION:RELATIVE;TOP:10;BORDER-LEFT-COLOR: gray; BORDER-BOTTOM-COLOR: #F46F20; WIDTH: 100%; BORDER-TOP-COLOR: FF9500; BORDER-COLLAPSE: collapse; BORDER-RIGHT-COLOR: gray\"";
    s += " borderColor=gray cellSpacing=0 cellPadding=5 border=1>\n";
    String [] detailrow = null;
    String [] classrow = null;
    for(int k = 0; k < count;k++){
      sql = "select xclassid from smclassheader where xsemester = '"+semester+"' and xcode='"+validcourse[k]+"' and xstatus<>'Full'";
      result = zabinfo.getSqlValues(sql);
      if(result !=null){
        s += "    <TR><TD WIDTH=20%>\n";
        s += "      <TABLE BORDER=0><TR><TD><SPAN class=regcourse>"+validcourse[k]+"  "+zabinfo.getLookupValue("smcourse.xlong(xcode = '"+validcourse[k]+"')")+"</SPAN></td></tr></table>\n</td>\n<td>";
        s += "<TABLE BORDER=1 cellSpacing=0 cellPadding=5  class=regcourse>\n";
        days = "";
        for(int i = 0; i < result.length;i++){
          sql = "select xsec,xcode,xcapacity,xcount,xstatus from smclassheader where xsemester = '"+semester+"' and xclassid='"+result[i]+"'";
          datarow = zabinfo.getSqlRow(sql);

          days += "<TR><TD align=center>\n";
          days += "<INPUT NAME=\"xselectcourse"+totalcount+"\"";
          days += " TYPE=CHECKBOX";
          days += " SIZE=50 VALUE=\""+result[i]+"\"";
          String ss = "";
          if(zabinfo.validclasses.containsKey(""+result[i])){
            days += "CHECKED";
          }
          days += "></td>";
          days += "<td align=center width=5%>"+datarow[0]+"</td>\n";
          sql = "select xrow from smclassdetail where xclassid = '"+result[i]+"'";
          detailrow = zabinfo.getSqlValues(sql);
//          s += "<th><th>Day<th>Class Type<th>Room<th>Time";
          if(detailrow != null){
            for(int j = 0; j<detailrow.length;j++){
              sql = "select xday,xclasstype,xroom,xtime  from smclassdetail where xclassid = '"+result[i]+"' and xrow= "+zabTools.getInt(detailrow[j])+"";
              classrow = zabinfo.getSqlRow(sql);
//              s += "<tr class=value><td></td>";
              days += "<td align=center width=5%>"+classrow[0]+"</td><td align=center>"+classrow[1]+"</td><td align=center>"+classrow[2]+"</td><td align=center>"+classrow[3]+"</td>\n";
            }
          }
          days += "</tr>\n";
//          s += days;
          totalcount = totalcount+1;
        }
        s += days;
        s += "</table>\n";
        s += "</td></tr>\n";
      }
    }
/*    s += "<TABLE>";
    s += "<TR><TD>";
    s += "</TD></TR>";
    s += "<TR><TD>";
    s += "</TD></TR>";
    s += "</TABLE>";
 */
    s +="</TABLE>\n";
    s +="<TABLE style=\"POSITION:RELATIVE;TOP:20\" class=regcourse>\n";
    s += "<TR><TD>";
    s += "<INPUT NAME=\"command\"";
    s += "style = \" WIDTH: 150px; HEIGHT: 20px\"";
    s += " TYPE=SUBMIT";
//    s += " TITLE=\""+f.help+"\"";
    s += " value=\"Next\"";
    s += "> ";
    s += "&nbsp;&nbsp;&nbsp;&nbsp;<INPUT NAME=\"command\"";
    s += "style = \" WIDTH: 150px; HEIGHT: 20px\"";
    s += " TYPE=SUBMIT";
//    s += " TITLE=\""+f.help+"\"";
    s += " value=\"Time Check\"";
    s += "> ";
    s += "</TD></TR>";
    s +="</TABLE>\n";
    s += "</FORM>";
//    s += "<DIV>";
//    System.out.println("Search "+zabinfo);
    smdata = zabinfo;
    return s;
  }

  public static String Search(zabInfo zabinfo){
    String s = "";
    String sql = "";
    String serial1 ="";
    String person1 = "";
    String room1 = "";
    String floor1 = "";
    String wing1 = "";
    String sup1 = "";
    String type1 = "";
    String manufacturer1 = "";
    String ipadd1 = "";
    String macadd1 = "";
    String [] donecourses = null;
    String [] notdone = null;
    String [] prereq = null;
    String [] validcourse = new String[1000];
//    String [] validcode = null;
    int count = 0;

    //GETTING DONE COURSES
//    zabinfo.ses.setAttribute("studentid",studentid);
//    zabinfo.ses.setAttribute("semester",semester);
    sql = "select xcode from smstudentcurriculum where xcoursecount='Yes' and xstudentid='"+studentid+"'";
    donecourses = zabinfo.getSqlValues(sql);

    //GETTING UNDONE COURSES
    sql = "select xcode from smstudentcurriculum where xcoursecount='No' and xstudentid='"+studentid+"'";
    notdone = zabinfo.getSqlValues(sql);
    String check = "0";
    if(notdone == null) return "";

    for(int i = 0; i < notdone.length;i++){
      check="0";

      //GETTING PREREQ OF EACH UNDONE COURSES

      sql = "select xprereq from smstudentcurriculum where xcode='"+notdone[i]+"' and xstudentid='"+studentid+"'";
      datarow = zabinfo.getSqlRow(sql);
      prereq = zabTools.parse(datarow[0],",");

      if(donecourses == null){
        if(prereq.length==0){
          check = "1";

//          continue;
        }

      }

//      if(prereq.length>0){
      for(int j = 0; j < prereq.length;j++){

        //CHECKING WITH DONE COURSES FOR VALID COURSES
        if(donecourses == null){
          if(prereq[j].equals("Nil") || prereq[j].equals("")){
            check = "1";
            break;
          }
        }else if(donecourses != null){

          for(int z = 0; z < donecourses.length;z++){
            if(prereq.length==1){
              if((prereq[j].equals(donecourses[z])) || (prereq[j].equals("Nil"))){
                check="1";
                break;
              }
            }else if(prereq.length>1){
              if(!(prereq[j].equals(donecourses[z]))){
                check = "0";
                //break;
              }else{
                check = "1";
                break;
              }
            }
          }
        }
        if(prereq.length==1 && check.equals("1")) break;
        if(prereq.length>1 && check.equals("0")) break;
      }
//      }
      //PUTING VALID COURSES
      if(check.equals("1")){
        validcourse[count]=notdone[i];
        count=count+1;
//System.out.println(" atcount"+count);
      }
    }
    String days = "";

//    s = semester;
//    s += "<DIV STYLE=\"POSITION:RELATIVE;TOP:50;backgound:white\">";
    s += "<FORM onSubmit=\"return submitIt(this)\" ACTION=\""+zabinfo.servlet+"\" METHOD=POST name=\"login\">\n";
    s += "<INPUT class=p NAME=screen TYPE=hidden VALUE=\"*smstudentprereg\">\n";

    s += "<TABLE BORDER=0 STYLE=\"POSITION:RELATIVE;TOP:20;\" WIDTH=100%><TR><TD>";
    s += "<SPAN style=\"font-weight:bold;font-size:10;font-family:verdana;\">Semester: "+semester+"  "+semestername+"</SPAN>";
    s += "</TD></TR>";
    s += "<TR><TD>";
//    s += "<TABLE WIDTH=100%><TR><TD>";
//    s += "<TD class=regth style=\"POSITION:RELATIVE;WIDTH:180;\">Course</TD><TD class=regth style=\"POSITION:RELATIVE;WIDTH:50;\">Check</TD><TD class=regth>Section</TD><TD class=regth>Day</TD><TD class=regth>Class Type</TD><TD class=regth>Room</TD><TD class=regth>Time</TD>";
//    s += "<TD class=regth>Day</TD><TD class=regth>Class Type</TD><TD class=regth>Room</TD><TD class=regth>Time</TD>";
//    s += "</TD></TR></TABLE>";
//    s += "<TABLE BORDER=10 WIDTH=100%>";
//    s += "  <TR><TD WIDTH=20%>Course</TD><TD>Check</TD><TD>Section</TD></TR>";
//    s += "<TR><TD colspan=2>";
    s += "<TABLE WIDTH=100%";
//    s += " style=\"POSITION:RELATIVE;TOP:10;BORDER-LEFT-COLOR: gray; BORDER-BOTTOM-COLOR: #F46F20; WIDTH: 100%; BORDER-TOP-COLOR: FF9500; BORDER-COLLAPSE: collapse; BORDER-RIGHT-COLOR: gray\"";
    s += " borderColor=gray cellSpacing=0 cellPadding=1 border=1>\n";
    String [] detailrow = null;
    String [] classrow = null;
    for(int k = 0; k < count;k++){
      sql = "select xclassid from smclassheader where xsemester = '"+semester+"' and xcode='"+validcourse[k]+"' and xstatus<>'Full'";
      result = zabinfo.getSqlValues(sql);
      if(result !=null){
        s += "<TR><TD WIDTH=20%>\n";
        s += "<TABLE BORDER=0><TR><TD><SPAN class=regcourse>"+validcourse[k]+"  "+zabinfo.getLookupValue("smcourse.xlong(xcode = '"+validcourse[k]+"')")+"</SPAN></td></tr></table>\n</td>\n<td>";
        s += "<TABLE BORDER=0 cellSpacing=0 cellPadding=0  class=regcourse>\n";
        days = "";
        for(int i = 0; i < result.length;i++){
          sql = "select xsec,xcode,xcapacity,xcount,xstatus from smclassheader where xsemester = '"+semester+"' and xclassid='"+result[i]+"'";
          datarow = zabinfo.getSqlRow(sql);

          days += "<TR><TD align=center>\n";
          days += "<INPUT NAME=\"xselectcourse"+totalcount+"\"";
          days += " TYPE=CHECKBOX";
          days += " SIZE=50 VALUE=\""+result[i]+"\"";
          String ss = "";
          if(zabinfo.validclasses.containsKey(""+result[i])){
            days += "CHECKED";
          }
          days += "></td>";
          days += "<td>("+datarow[0]+"\n";
          sql = "select xrow from smclassdetail where xclassid = '"+result[i]+"'";
          detailrow = zabinfo.getSqlValues(sql);
//          s += "<th><th>Day<th>Class Type<th>Room<th>Time";
          if(detailrow != null){
            for(int j = 0; j<detailrow.length;j++){
              sql = "select xday,xclasstype,xroom,xtime  from smclassdetail where xclassid = '"+result[i]+"' and xrow= "+zabTools.getInt(detailrow[j])+"";
              classrow = zabinfo.getSqlRow(sql);
//              s += "<tr class=value><td></td>";
              days += ","+classrow[0]+","+classrow[1]+","+classrow[2]+","+classrow[3]+"\n";
            }
          }
          days += ")<td></tr>\n";
//          s += days;
          totalcount = totalcount+1;
        }
        s += days;
        s += "</table>\n";
        s += "</td></tr>\n";
      }
    }
/*    s += "<TABLE>";
    s += "<TR><TD>";
    s += "</TD></TR>";
    s += "<TR><TD>";
    s += "</TD></TR>";
    s += "</TABLE>";
 */
    s +="</TABLE>\n";
    s +="<TABLE style=\"POSITION:RELATIVE;TOP:20\" class=regcourse>\n";
    s += "<TR><TD>";
    s += "<INPUT NAME=\"command\"";
    s += "style = \" WIDTH: 150px; HEIGHT: 20px\"";
    s += " TYPE=SUBMIT";
//    s += " TITLE=\""+f.help+"\"";
    s += " value=\"Next\"";
    s += "> ";
    s += "&nbsp;&nbsp;&nbsp;&nbsp;<INPUT NAME=\"command\"";
    s += "style = \" WIDTH: 150px; HEIGHT: 20px\"";
    s += " TYPE=SUBMIT";
//    s += " TITLE=\""+f.help+"\"";
    s += " value=\"Time Check\"";
    s += "> ";
    s += "</TD></TR>";
    s += "</TABLE>\n";
    s += "</TD></TR></TABLE>";
    s += "</FORM>";
//    s += "<DIV>";
//    System.out.println("Search "+zabinfo);
    smdata = zabinfo;
    return s;
  }

  public static String Registration(zabInfo zabinfo){
    String s = "";
    String sql = "";
    double totcredit = 0;
    double clab = 0;
    double slab = 0;
    double totalcost=0;
    double tution = 0;
    String amt = "";
    String degree = ""+zabinfo.getLookupValue("smstudent.xdegree(xstudentid = '"+studentid+"')");
//    String department = ""+zabinfo.getLookupValue("smstudent.xdepartment(xstudentid = '"+studentid+"')");
//    String semester = (String)zabinfo.ses.getAttribute("semester");
//    String studentid = (String)zabinfo.ses.getAttribute("studentid");
//    String addsemester = ""+zabinfo.getLookupValue("smstudent.xsemester(xstudentid = '"+studentid+"')");

    s += "<FORM onSubmit=\"return submitIt(this)\" ACTION=\""+zabinfo.servlet+"\" METHOD=POST name=\"login\">";
    s += "<INPUT class=p NAME=screen TYPE=hidden VALUE=\"*smconfirmreg\">";
    Enumeration en = smdata.validclasses.elements();


    s += "<TABLE BORDER=0 cellspacing=0 cellpadding=3 WIDTH=100% class=regcourse>";
    s += "<TR><TD>";

    s += "<TABLE BORDER=0 WIDTH=100% class=regcourse>";
    s += "<TH class=regth>Course</TH><TH class=regth>Credit</TH><TH class=regth>Selected Section</TH>";

    while(en.hasMoreElements()){
      Object o = en.nextElement();
        String code = ""+zabinfo.getLookupValue("smclassheader.xcode(xclassid = '"+o+"')");
        String desc = ""+zabinfo.getLookupValue("smstudentcurriculum.xlong(xcode = '"+code+"')");
        String credit = ""+zabinfo.getLookupValue("smstudentcurriculum.xcredit(xcode = '"+code+"')");
        String section = ""+zabinfo.getLookupValue("smclassheader.xsec(xclassid = '"+o+"')");
        s +="<TR><TD class=DataGrid WIDTH=40%>"+desc+" - "+desc+"</TD><TD align=middle class=DataGrid>"+credit+"</TD><TD class=DataGrid WIDTH=40%>"+desc+",  Sec - "+section+"</TD></TR>";
    }
    s += "</TABLE></TD></TR>";

    s += "<TR><TD>";

/*
    sql = "select xrow from smstudentcurriculum where xsemester='"+semester+"' and xstudentid='"+studentid+"' and xcoursecount='Yes'";
    result = zabinfo.getSqlValues(sql);
    if(result != null){
      s += "<TABLE BORDER=0 WIDTH=100% class=regcourse>";
      s += "<TH class=regth>Course</TH><TH class=regth>Credit</TH><TH class=regth>Selected Class</TH>";
      for(int i = 0; i < result.length;i++){
        sql = "select xcode,xlong,xcredit,xclassid from smstudentcurriculum where xsemester='"+semester+"' and xstudentid='"+studentid+"' and xrow="+zabTools.getInt(result[i])+"";
        datarow = zabinfo.getSqlRow(sql);
        String section = ""+zabinfo.getLookupValue("smclassheader.xsec(xclassid = '"+datarow[3]+"')");
        s +="<TR><TD class=DataGrid WIDTH=40%>"+datarow[0]+" - "+datarow[1]+"</TD><TD align=middle class=DataGrid>"+datarow[2]+"</TD><TD class=DataGrid WIDTH=40%>"+datarow[1]+", "+section+"</TD></TR>";
      }
      s += "</TABLE>";
    }
*/
//  s +="<DIV style=\"POSITION:absolute;TOP:180;WIDHT:250;left:5\">";
    s += "<TABLE BORDER=0 class=regcourse WIDTH=40%>";
    s += "<TH class=regth>Payment Type</TH><TH class=regth>Amount</TH>";
    en = smdata.validclasses.elements();
    while(en.hasMoreElements()){
      Object o = en.nextElement();
      String labc = "";
      String labs = "";
      totcredit = totcredit+zabTools.getDouble(""+zabinfo.getLookupValue("smstudentcurriculum.xcredit(xcode = '"+""+zabinfo.getLookupValue("smclassheader.xcode(xclassid = '"+o+"')")+"' and xstudentid='"+studentid+"')"));
      labc = ""+zabinfo.getLookupValue("smstudentcurriculum.xclab(xcode = '"+""+zabinfo.getLookupValue("smclassheader.xcode(xclassid = '"+o+"')")+"' and xstudentid='"+studentid+"')");
      if(labc == null || labc.equals("null")) labc="";
      if(labc.equals("1")) clab=clab+1;

      labs = ""+zabinfo.getLookupValue("smstudentcurriculum.xslab(xcode = '"+""+zabinfo.getLookupValue("smclassheader.xcode(xclassid = '"+o+"')")+"' and xstudentid='"+studentid+"')");
      if(labs == null || labs.equals("null")) labs="";
      if(labs.equals("1")) slab=slab+1;
    }

    sql = "select xrow from smstudentfees where xstudentid='"+studentid+"'";
    result = zabinfo.getSqlValues(sql);
    totFees.clear();
    for(int i =0; i<result.length;i++){
      s += "<TR>";
//      amt = "<td>0.00</td>";
//      s += "<td>"+result[i]+"</td>"+amt;

      sql = "select xtype,xitem,xamount,xsign from smstudentfees where xrow="+zabTools.getInt(result[i])+" and xstudentid='"+studentid+"'";
      datarow=zabinfo.getSqlRow(sql);
      amt = "<TD class=DataGrid WIDTH 20%>"+datarow[1]+"</td><td class=DataGrid  align=right>0.00</td></tr>";
      if(addsemester.equals(semester)){
        if(datarow[0].equals("One Time")){
          amt = "<TD class=DataGrid WIDTH 20%>"+datarow[1]+"</td><TD class=DataGrid align=right>"+datarow[2]+"</td></tr>";
          totFees.put(datarow[1],datarow[2]);
          totalcost = totalcost+(zabTools.getDouble(datarow[2])*zabTools.getDouble(datarow[3]));
        }
        if(datarow[0].equals("Yearly")){
          amt = "<TD class=DataGrid WIDTH 30%>"+datarow[1]+"</td><TD class=DataGrid align=right>"+datarow[2]+"</td></tr>";
          totFees.put(datarow[1],datarow[2]);
          totalcost = totalcost+(zabTools.getDouble(datarow[2])*zabTools.getDouble(datarow[3]));
        }
        if(datarow[0].equals("Semester")){
          amt = "<TD class=DataGrid>"+datarow[1]+"</td><TD class=DataGrid align=right>"+datarow[2]+"</td></tr>";
          totFees.put(datarow[1],datarow[2]);
          totalcost = totalcost+(zabTools.getDouble(datarow[2])*zabTools.getDouble(datarow[3]));
        }
      }else{
        if(datarow[0].equals("Yearly")){
          sql = "select count(distinct xsemester) from smstudentcurriculum where xsemester<>''";
          sa = zabinfo.getSqlValues(sql);
          if(zabTools.getDouble(sa[0]) % 4 == 0){
            amt = "<TD class=DataGrid>"+datarow[1]+"</td><TD class=DataGrid align=right>"+datarow[2]+"</td></tr>";
            totFees.put(datarow[1],datarow[2]);
            totalcost = totalcost+(zabTools.getDouble(datarow[2])*zabTools.getDouble(datarow[3]));
          }
        }
        if(datarow[0].equals("Semester")){
          amt = "<TD class=DataGrid>"+datarow[1]+"</td><TD class=DataGrid align=right>"+datarow[2]+"</td></tr>";
          totFees.put(datarow[1],datarow[2]);
          totalcost = totalcost+(zabTools.getDouble(datarow[2])*zabTools.getDouble(datarow[3]));
        }
      }
      double total = 0;
//    tution = 0;
      if(datarow[0].equals("Clab")){
        total = clab*zabTools.getDouble(datarow[2]);
        amt = "<TD class=DataGrid>"+datarow[1]+"</td><TD class=DataGrid align=right>"+total+"</td></tr>";
        if(total >0){
          totFees.put(datarow[1],""+total);
          totalcost = totalcost+(total*zabTools.getDouble(datarow[3]));
        }
      }
      if(datarow[0].equals("Slab")){
        total = slab*zabTools.getDouble(datarow[2]);
        amt = "<TD class=DataGrid>"+datarow[1]+"</td><TD class=DataGrid align=right>"+total+"</td></tr>";
        if(total >0){
          totFees.put(datarow[1],""+total);
          totalcost = totalcost+(total*zabTools.getDouble(datarow[3]));
        }
      }

      if(datarow[0].equals("Credit")){
        total = totcredit*zabTools.getDouble(datarow[2]);
        amt = "<TD class=DataGrid>"+datarow[1]+"</td><TD class=DataGrid align=right>"+total+"</td></tr>";
        if(total >0){
          tution = total;
          totFees.put(datarow[1],""+total);
          totalcost = totalcost+(total*zabTools.getDouble(datarow[3]));
        }
      }
      if(datarow[0].equals("Other")){
        if(zabTools.getDouble(datarow[2])>0){
          amt = "<TD class=DataGrid>"+datarow[1]+"</td><TD class=DataGrid align=right>"+datarow[2]+"</td></tr>";
          totFees.put(datarow[1],""+datarow[2]);
          totalcost = totalcost+(zabTools.getDouble(datarow[2])*zabTools.getDouble(datarow[3]));
        }
      }
      s += amt;
    }
    s += "<TR><TD class=DataGrid>Total</td><TD class=DataGrid align=right>"+totalcost+"</td></TR>\n";
    s += "<TR><TD>";
    s += "<INPUT NAME=\"command\"";
    s += "style = \" WIDTH: 150px; HEIGHT: 20px\"";
    s += " TYPE=SUBMIT";
    s += " VALUE=\"Submit Registration\"";
    s += " SIZE=20> ";
    s += "&nbsp;&nbsp;&nbsp;&nbsp;<INPUT class=p NAME=\"command\"";
    s += "style = \" WIDTH: 150px; HEIGHT: 20px\"";
    s += " TYPE=SUBMIT";
    s += " value=\"Back\"";
    s += "> ";
    s += "</TD></TR>";
    s += "</TABLE></TD></TR></TABLE>\n";
    s += "</FORM>";
    //    s += "</DIV>";
/*    s += "<TABLE><TR></TR>\n";
    s += "<TR><td>\n";
    s += "Select Your Payment Type";
    s += "</TR></td>";
    s += "<TR><td>";
    s += "<INPUT class=field NAME=\"Payment Type\"";
    s += " TYPE=RADIO";
    s += " SIZE=50 VALUE=\"Cash Payment\">Cash Payment";
    s += "</td></TR>";
    s += "<TR><td>";
    s += "<INPUT class=field NAME=\"Payment Type\"";
    s += " TYPE=RADIO";
    s += " SIZE=50 VALUE=\"2 Installments\">2 Installments";
    s += "</td></TR>";
    s += "<TR><td>";
    s += "<INPUT class=field NAME=\"Payment Type\"";
    s += " TYPE=RADIO";
    s += " SIZE=50 VALUE=\"3 Installments\" CHECKED>3 Installments";
    s += "</td></TR></TABLE>";
    s += "<TABLE>";
    s += "<TR><TD>";

    s += "Cash Payment :"+totalcost+"";
    s += "</TD></TR>";
    s += "<TR>";
    s += "</TR>";

    s += "<TH>";
    s += "2 Installments ";
    s += "</TH>";
    s += "<TR><TD>";
    clab = totalcost-tution;
    slab = (tution*30/100)+clab;
    s += "1st Installment :"+slab+"";
    s += "</TD></TR>";
    s += "<TR><TD>";
    slab = tution-(tution*30/100);
    s += "2nd Installment :"+slab+"";
    s += "</TD></TR>";
    s += "<TR><TD>";
    s += "</TD></TR>";

    s += "<TH>";
    s += "3 Installments ";
    s += "</TH>";
    s += "<TR><TD>";
    clab = totalcost-tution;
    clab = (tution*30/100)+clab;
    s += "1st Installment :"+clab+"";
    s += "</TD></TR>";
    s += "<TR><TD>";
    clab = tution-(tution*30/100);
    slab = (clab*30/100);
//    totalcost =(tution*30/100);
    s += "2nd Installment :"+slab+"";
    s += "</TD></TR>";
    s += "<TR><TD>";
//    clab = clab-(clab*30/100);
    slab = clab-slab;
//    totalcost =(tution*30/100);
//    tution = tution-(tution*30/100);
    s += "3rd Installment :"+slab+"";
    s += "</TD></TR>";
    s += "</TABLE>";



    s += "<INPUT class=p NAME=\"command\"";
    s += " TYPE=SUBMIT";
    s += " value=\"Ok\"";
    s += "> ";
    s += "&nbsp;&nbsp;&nbsp;&nbsp;<INPUT class=p NAME=\"command\"";
    s += " TYPE=SUBMIT";
    s += " value=\"Back\"";
    s += "> ";
    s += "</DIV>";
*/
    return s;
   }

  public static String StudentRegistration(zabInfo zabinfo){
    smdata=zabinfo;
    String s = "";
    String sql = "";
    double totcredit = 0;
    double clab = 0;
    double slab = 0;
    double totalcost=0;
    double tution = 0;
    String amt = "";
    String degree = ""+zabinfo.getLookupValue("smstudent.xdegree(xstudentid = '"+studentid+"')");
    String department = ""+zabinfo.getLookupValue("smstudent.xdepartment(xstudentid = '"+studentid+"')");
//    s +="<DIV style=\"POSITION:absolute;TOP:10;left:5\">";

    s += "<FORM onSubmit=\"return submitIt(this)\" ACTION=\""+zabinfo.servlet+"\" METHOD=POST name=\"login\">\n";
    s += "<INPUT class=p NAME=screen TYPE=hidden VALUE=\"*smstudentreg\">\n";

    //    Enumeration en = smdata.validclasses.elements();

    s += "<TABLE BORDER=0 WIDTH=100% class=regcourse>\n";
    s += "<TR><TD colspan=2>\n";

//    s += "<TABLE BORDER=0 WIDTH=100% class=regcourse>";
//    s += "<TH class=regth>Course</TH><TH class=regth>Credit</TH><TH class=regth>Selected Section</TH>";
/*
    while(en.hasMoreElements()){
      Object o = en.nextElement();
        String code = ""+zabinfo.getLookupValue("smclassheader.xcode(xclassid = '"+o+"')");
        String desc = ""+zabinfo.getLookupValue("smstudentcurriculum.xlong(xcode = '"+code+"')");
        String credit = ""+zabinfo.getLookupValue("smstudentcurriculum.xcredit(xcode = '"+code+"')");
        String section = ""+zabinfo.getLookupValue("smclassheader.xsec(xclassid = '"+o+"')");
        s +="<TR><TD class=DataGrid WIDTH=40%>"+desc+" - "+desc+"</TD><TD align=middle class=DataGrid>"+credit+"</TD><TD class=DataGrid WIDTH=40%>"+desc+",  Sec - "+section+"</TD></TR>";
    }
    s += "</TABLE>";
*/

    sql = "select xrow from smstudentcurriculum where xsemester='"+semester+"' and xstudentid='"+studentid+"' and xcoursecount='Yes'";
    result = zabinfo.getSqlValues(sql);
    if(result != null){
      s += "<TABLE BORDER=0 WIDTH=100% class=regcourse>\n";
      s += "<TH class=regth>Course</TH><TH class=regth>Credit</TH><TH class=regth>Selected Class</TH>\n";
      for(int i = 0; i < result.length;i++){
        sql = "select xcode,xlong,xcredit,xclassid from smstudentcurriculum where xsemester='"+semester+"' and xstudentid='"+studentid+"' and xrow="+zabTools.getInt(result[i])+"";
        datarow = zabinfo.getSqlRow(sql);
        String section = ""+zabinfo.getLookupValue("smclassheader.xsec(xclassid = '"+datarow[3]+"')");
        s +="<TR><TD class=DataGrid WIDTH=40%>"+datarow[0]+" - "+datarow[1]+"</TD><TD align=middle class=DataGrid>"+datarow[2]+"</TD><TD class=DataGrid WIDTH=40%>"+datarow[1]+", "+section+"</TD></TR>\n";
      }
      s += "</TABLE>\n";
    }
    s += "</TD></TR>\n";
    s += "<TR><TD align=left WIDTH=50%>\n";

    //********PAYMENT TYPE**************

    //    s +="<DIV style=\"POSITION:RELATIVE;TOP:10;left:0\">";
    s += "<TABLE BORDER=0 class=regcourse WIDTH=98%>\n";
    s += "<TH class=regth>Payment Type</TH><TH class=regth>Amount</TH>\n";

    sql = "select xrow from smstudentfeesdetail where xstudentid='"+studentid+"' and xsemester='"+semester+"'";
    result = zabinfo.getSqlValues(sql);
    if(result == null)
      return "<FONT SIZE=+2 COLOR=red>Fees Not Updated</FONT>";

    for(int i =0; i<result.length;i++){

      sql = "select xitem,xamount,xsign,xtype from smstudentfeesdetail where xrow="+zabTools.getInt(result[i])+" and xstudentid='"+studentid+"'";
      datarow=zabinfo.getSqlRow(sql);
      totalcost = totalcost+zabTools.getDouble(datarow[1])*zabTools.getDouble(datarow[2]);
      if(datarow[3].equalsIgnoreCase("credit"))
        tution = zabTools.getDouble(datarow[1]);
      s += "<TR><TD class=DataGrid WIDTH 20%>"+datarow[0]+"</TD>\n";
      s += "<TD class=DataGrid align=right>"+datarow[1]+"</TR>\n";
    }
    s += "<TR><TD class=DataGrid>Total</TD><TD class=DataGrid align=right>"+totalcost+"</TD></TR>\n";
    totalfees=totalcost;
/*    s += "<INPUT NAME=\"command\"";
    s += "style = \" WIDTH: 150px; HEIGHT: 20px\"";
    s += " TYPE=SUBMIT";
    s += " VALUE=\"Submit Registration\"";
    s += " SIZE=20> ";
    s += "&nbsp;&nbsp;&nbsp;&nbsp;<INPUT class=p NAME=\"command\"";
    s += "style = \" WIDTH: 150px; HEIGHT: 20px\"";
    s += " TYPE=SUBMIT";
    s += " value=\"Back\"";
    s += "> ";
*///    s += "</TD></TR>";
    s += "</TABLE></TD>\n";
    s += "<TD align=center valign=top WIDTH=48%>\n";


    s += "<TABLE class=regcourse border=0 WIDTH=100%>\n";
    s += "<TH class=regth colspan=2>Single Payment</TH><TH class=regth colspan=2>3 Payments</TH>\n";
    s += "<TR><TD class=DataGrid>Cash Payment </TD>\n";
    s += "<TD class=DataGrid align=right>"+totalcost+"</TD>\n";

    clab = totalcost-tution;
    clab = (tution*30/100)+clab;
    firstinst=clab;
    s += "<TD class=DataGrid>";
    s += "1st Installment </TD>\n";
    s += "<TD class=DataGrid align=right>"+clab+"</TD>\n";
    clab = tution-(tution*30/100);
    slab = (clab*30/100);
    secinst=slab;
    s += "<TR><TD class=DataGrid align=middle> - </TD><TD class=DataGrid align=middle> - </TD>\n";
    s += "<TD class=DataGrid>2nd Installment</TD>\n" ;
    s += "<TD class=DataGrid align=right>"+slab+"</TD></TR>\n";
    slab = clab-slab;
    thirdinst=slab;
    s += "<TR><TD class=DataGrid align=middle> - </TD><TD class=DataGrid align=middle> - </TD>\n";
    s += "<TD class=DataGrid>";
    s += "3rd Installment </TD>\n";
    s += "<TD class=DataGrid align=right>"+slab+"</TD>";
    s += "</TR>\n";
    s += "</TABLE></TD></TR>\n";




    s += "<TR><TD>\n";
//    s += "</DIV>";

//    s += "<DIV style=\"POSITION:absolute;TOP:90;left:525;WIDTH:450\">";
//    s += "var scrwidth = ";                        (screen.availWidth)/
//    s += "<DIV style=\"POSITION:absolute;TOP:30%;left:50%;WIDTH:50%\">";



    s +="<TABLE BORDER=0>\n";
    s += "<TR><TD>\n";
    s += "<SPAN style=\"FONT-WEIGHT: bold;FONT-SIZE:0.8em; FONT-FAMILY: Tahoma,Arial,Verdana, sans-serif;\">Select Your Payment Type</SPAN>";
    s += "</TR></TD>\n";
    s += "<TR><TD>\n";
    s += "<INPUT class=field NAME=\"Payment Type\"";
    s += " TYPE=RADIO";
    s += " SIZE=50 VALUE=\"Cash Payment\">";
    s += "<SPAN style=\"FONT-SIZE:0.8em; FONT-FAMILY: Tahoma,Arial,Verdana, sans-serif;\">Cash Payment</SPAN>";
    s += "</TD></TR>\n";
    s += "<TR><TD>\n";
    s += "<INPUT class=field NAME=\"Payment Type\"";
    s += " TYPE=RADIO";
    s += " SIZE=50 VALUE=\"3 Installments\" CHECKED>";
    s += "<SPAN style=\"FONT-SIZE:0.8em; FONT-FAMILY: Tahoma,Arial,Verdana, sans-serif;\">3 Installments</SPAN>";
    s += "</TD></TR></TABLE></TD></TR>\n";



    s += "</TABLE>";
//    s += "</DIV>";

/*    s += "<TH>";
//    s += "2 Installments ";
//    s += "</TH>";
//    s += "<TR><TD>";
//    clab = totalcost-tution;
//    slab = (tution*30/100)+clab;
    s += "1st Installment :"+slab+"";
    s += "</TD></TR>";
    s += "<TR><TD>";
    slab = tution-(tution*30/100);
    s += "2nd Installment :"+slab+"";
    s += "</TD></TR>";
    s += "<TR><TD>";
    s += "</TD></TR>";

    s += "<TH>";
    s += "3 Installments ";
    s += "</TH>";
*/

    s += "<INPUT NAME=\"command\"";
    s += " onclick=javascript:printPage()";
    s += "style = \" WIDTH: 150px; HEIGHT: 20px\"";
    s += " TYPE=SUBMIT";
    s += " value=\"Ok\"";
    s += "> ";

//    s += "</DIV>";
    s += "</form>";
    return s;
   }

  public static String StudentCurriculum(zabInfo zabinfo){
    String s = "";
    String check = "";
    String sql = "";
    String [] result = null;
    String [] datarow = null;
    String [] detailrow = null;
    String [] resultrow = null;
    String [] comparerow = null;
    double totalcredit = zabTools.getDouble(zabinfo.getSummaryValue("smstudentcurriculum", "xcredit", "xstudentid = '"+studentid+"' and xstatus<>'Retake'", "sum").toString());

    double credit = 0;
//    String degree = ""+zabinfo.getLookupValue("smstudent.xdegree(xstudentid = '"+studentid+"')");
    department = ""+zabinfo.getLookupValue("smstudent.xdepartment(xstudentid = '"+studentid+"')");

//    department = ""+zabinfo.getLookupValue("xcodes.xlong(xtype='Department' and xcode = '"+""+zabinfo.getLookupValue("smstudent.xdepartment(xstudentid = '"+studentid+"')")+"')");
    sql = "select distinct xdesc from smstudentcurriculum where xstudentid='"+studentid+"'";
    result = zabinfo.getSqlValues(sql);

    if(result == null) return "";
                                                                           // cellspacing=2 cellpadding=6
    s += "<TABLE style=\"POSITION:relative;top:20;\" cellspacing=0 cellpadding=4 align=center BORDER=0 WIDTH=98% >\n";

    s += "<TR><TD>\n";
                 //
    s += "<TABLE BORDER=0 cellspacing=0 cellpadding=0 WIDTH=100%>\n";
    s += "<TR><TD align=center>";
    s += "<SPAN STYLE=\"FONT-FAMILY: Verdana, sans-serif; FONT-SIZE:1.5em;FONT-WEIGHT:BOLD;\">UNITED  INTERNATIONAL  UNIVERSITY</SPAN>";
    s += "</TD></TR>";
    s += "<TR><TD align=center>";
    s += "<SPAN STYLE=\"FONT-FAMILY: Verdana, sans-serif; FONT-SIZE:1.0em;FONT-WEIGHT:BOLD;\">Grade Report By Curriculum</SPAN>";
    s += "</TD></TR>";
    s += "</TABLE></TD></TR>";

    s += "<TR><TD>\n";
                 //cellspacing=0 cellpadding=0
    s += "<TABLE BORDER=0 WIDTH=100%>\n";
    s += "<TR><TD align=left WIDTH=50%>";
                                                                                                   //cellspacing=2 cellpadding=0
    s += "<TABLE align=center style=\"FONT-FAMILY: Verdana, sans-serif; FONT-SIZE: 0.7em; LINE-HEIGHT: 1.58em\" BORDER=0 WIDTH=100%>\n";

    s += "<TR>\n";
    s += "<TD>Student id</TD><TD>"+studentid+"</TD>";
    s += "</TR>\n";
    s += "<TR>\n";
    s += "<TD>Student Name</TD><TD>"+fname+" "+mname+" "+lname+"</TD>\n";
    s += "</TR>\n";
    s += "<TR>\n";
    s += "<TD>Total Credit </TD><TD>"+totalcredit+"</TD>";
    s += "</TR>\n";
    s += "<TR>\n";
    s += "<TD>CGPA</TD><TD>"+cgpa+"</TD>\n";

    s += "</TR>\n";
    s += "</TABLE></TD>\n";

    s += "<TD valign=top align=right WIDTH=50%>";                                        //          cellspacing=2 cellpadding=0

    s += "<TABLE style=\"FONT-FAMILY: Verdana, sans-serif; FONT-SIZE: 0.7em; LINE-HEIGHT: 1.58em\" BORDER=0 WIDTH=60%>\n";
    s += "<TR>";

    s += "<TD align=right >Department</TD><TD align=right >"+department+", "+addsemester+"</TD>";
    s += "</TR>";
    s += "</TABLE></TD>";

    s += "</TR>";
    s += "</TABLE></TD></TR>";

    for(int i = 0; i <result.length;i++){
      
      s += "<TR><TD class=regcourse>"+result[i]+"</TD></TR>\n";
      s += "<TR><TD align=center>";

      s += "<TABLE BORDER=0 style=\"FONT-FAMILY: Verdana, sans-serif; FONT-SIZE: 0.7em; LINE-HEIGHT: 1.58em\" cellspacing=0 cellpadding=2 WIDTH=100% >\n";

      s += "<TH class=regth>Course ID</TH><TH class=regth>Course Title</TH><TH class=regth>Credit</TH><TH class=regth style=\"BORDER-RIGHT: darkgray 1px solid\" >Prerequisite(s)</TH>\n";
      credit = 0;

      sql = "select xrow from smstudentcurriculum where xstudentid='"+studentid+"' and xdesc='"+result[i]+"' and xstatus<>'Retake'";
      resultrow = zabinfo.getSqlValues(sql);

//      if(resultrow ==null)
//        return "";

      for(int j = 0; j <resultrow.length;j++){

        sql = "select xcode,xlong,xcredit,xprereq from smstudentcurriculum where xstudentid='"+studentid+"' and xrow="+zabTools.getInt(resultrow[j])+"";
        datarow = zabinfo.getSqlRow(sql);

        credit = credit+zabTools.getDouble(datarow[2]);
        s += "<TR>";
        s += "<TD class=p>"+datarow[0]+"</TD>";
        s += "<TD WIDTH=400px>"+datarow[1]+"</TD>";
        s += "<TD align=center>"+datarow[2]+"</TD>";
        s += "<TD>"+datarow[3]+"</TD>";
        s += "</TR>";
        }
      s += "</TABLE><TD><TR>\n";
    }
    java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

    s += "<TR><TD><SPAN style=\"FONT-FAMILY: Tahoma,Verdana, sans-serif; FONT-SIZE: 0.7em; LINE-HEIGHT: 1.58em\">";
    s += "Printed By "+fname+" "+mname+" "+lname;
    s += "</SPAN></TD></TR>";
    s += "<TR><TD><SPAN style=\"FONT-FAMILY: Tahoma,Verdana, sans-serif; FONT-SIZE: 0.7em; LINE-HEIGHT: 1.58em\">";
    s += "Print Time "+zabTools.dateDisplay(today.toString(),'D','-') ;
    s += "</SPAN></TD></TR>";
    s += "</TABLE>\n";
    return s;

   }

  public static String GradeReportByCurriculum(zabInfo zabinfo){
    String s = "";
    String dataline = "";
    String grade = "";
    String sql = "";
    String [] result = null;
    String [] datarow = null;
    String [] detailrow = null;
    String [] resultrow = null;
    String [] comparerow = null;
//    String degree = ""+zabinfo.getLookupValue("smstudent.xdegree(xstudentid = '"+studentid+"')");
    department = ""+zabinfo.getLookupValue("smstudent.xdepartment(xstudentid = '"+studentid+"')");

//    department = ""+zabinfo.getLookupValue("xcodes.xlong(xtype='Department' and xcode = '"+""+zabinfo.getLookupValue("smstudent.xdepartment(xstudentid = '"+studentid+"')")+"')");
    sql = "select distinct xdesc from smstudentcurriculum where xstudentid='"+studentid+"'";
    result = zabinfo.getSqlValues(sql);

    if(result == null) return "";
                                                                           // cellspacing=2 cellpadding=6
    s += "<TABLE style=\"POSITION:relative;top:20;\" cellspacing=0 cellpadding=4 align=center BORDER=0 WIDTH=98% >\n";

    s += "<TR><TD>\n";
                 //
    s += "<TABLE BORDER=0 cellspacing=0 cellpadding=0 WIDTH=100%>\n";
    s += "<TR><TD align=center>";
    s += "<SPAN STYLE=\"FONT-FAMILY: Verdana, sans-serif; FONT-SIZE:1.5em;FONT-WEIGHT:BOLD;\">UNITED  INTERNATIONAL  UNIVERSITY</SPAN>";
    s += "</TD></TR>";
    s += "<TR><TD align=center>";
    s += "<SPAN STYLE=\"FONT-FAMILY: Verdana, sans-serif; FONT-SIZE:1.0em;FONT-WEIGHT:BOLD;\">Grade Report By Curriculum</SPAN>";
    s += "</TD></TR>";
    s += "</TABLE></TD></TR>";

    s += "<TR><TD>\n";
                 //cellspacing=0 cellpadding=0
    s += "<TABLE BORDER=0 WIDTH=100%>\n";
    s += "<TR><TD align=left WIDTH=50%>";
                                                                                                   //cellspacing=2 cellpadding=0
    s += "<TABLE align=center style=\"FONT-FAMILY: Verdana, sans-serif; FONT-SIZE: 0.7em; LINE-HEIGHT: 1.58em\" BORDER=0 WIDTH=100%>\n";

    s += "<TR>\n";
    s += "<TD>Student id</TD><TD>"+studentid+"</TD>";
    s += "</TR>\n";
    s += "<TR>\n";
    s += "<TD>Student Name</TD><TD>"+fname+" "+mname+" "+lname+"</TD>\n";
    s += "</TR>\n";
    s += "<TR>\n";
    s += "<TD>Credit(s) Completed </TD><TD>"+zabinfo.getSummaryValue("smstudentcurriculum", "xcredit", "xstudentid = '"+studentid+"'", "sum").toString()+"</TD>";
    s += "</TR>\n";
    s += "<TR>\n";
    s += "<TD><B>CGPA</B></TD><TD>"+cgpa+"</TD>\n";

    s += "</TR>\n";
    s += "</TABLE></TD>\n";

    s += "<TD valign=top align=right WIDTH=50%>";                                        //          cellspacing=2 cellpadding=0

    s += "<TABLE style=\"FONT-FAMILY: Verdana, sans-serif; FONT-SIZE: 0.7em; LINE-HEIGHT: 1.58em\" BORDER=0 WIDTH=60%>\n";
    s += "<TR>";

    s += "<TD align=right >Department</TD><TD align=right >"+department+", "+addsemester+"</TD>";
    s += "</TR>";
    s += "</TABLE></TD>";

    s += "</TR>";
    s += "</TABLE></TD></TR>";

//    s += "<TR><TD>";
    for(int i = 0; i <result.length;i++){
                                             // cellspacing=2 cellpadding=4
//      s += "<TABLE  BORDER=0 cellspacing=0 cellpadding=0 WIDTH=100%>\n";
//      s += "<TR><TD class=regcourse  >"+result[i]+"</TD></TR>\n";
      s += "<TR><TD class=regcourse>"+result[i]+"</TD></TR>\n";
//      s += "<TR><TD></TD></TR>";
//      s += "<TR><TD></TD></TR>";
//      s += "<TR></TR>";
//      s += "</TABLE>\n";
                            //
//      s += "</TD></TR>";
      s += "<TR><TD align=center>";

//      s += "<TABLE BORDER=0 class=DataGrid cellspacing=0 cellpadding=0 >\n";
//      s += "<TR><TD>";

      s += "<TABLE BORDER=0 cellspacing=0 cellpadding=2 WIDTH=100% >\n";

      s += "<TH class=regth>Course</TH><TH class=regth>Credit</TH><TH class=regth>Passing Semester</TH><TH class=regth style=\"BORDER-RIGHT: darkgray 1px solid\" >Grade</TH>\n";

      sql = "select distinct xcode from smstudentcurriculum where xstudentid='"+studentid+"' and xdesc='"+result[i]+"'";
      resultrow = zabinfo.getSqlValues(sql);

      if(resultrow ==null)
        return "";
      for(int j = 0; j <resultrow.length;j++){

        sql = "select xlong,xcredit,xsemester,xfinalgrade from smstudentcurriculum where xstudentid='"+studentid+"' and xcode='"+resultrow[j]+"'";
        datarow = zabinfo.getSqlRow(sql);

//        if(j % 2 == 0){
        if(j == resultrow.length-1){
          dataline ="<TR><TD class=DataGrid  style=\"BORDER-BOTTOM: darkgray 1px solid\" >"+resultrow[j]+" - "+datarow[0]+"</TD>";
          dataline += "<TD align=middle class=DataGrid  style=\"BORDER-BOTTOM: darkgray 1px solid\" WIDTH=40px >"+datarow[1]+"</TD>";
          dataline += "<TD class=DataGrid  style=\"BORDER-BOTTOM: darkgray 1px solid\"  WIDTH=140px align=middle>";
          grade = "<TD class=DataGrid style=\"BORDER-BOTTOM: darkgray 1px solid\" width=40px align=middle>";
        }else{
        dataline ="<TR><TD class=DataGrid >"+resultrow[j]+" - "+datarow[0]+"</TD>";
        dataline += "<TD align=middle class=DataGrid WIDTH=40px >"+datarow[1]+"</TD>";
        dataline += "<TD class=DataGrid  WIDTH=140px align=middle>";
          grade = "<TD class=DataGrid  style=\"BORDER-BOTTOM: darkgray 1px solid\" width=40px align=middle>";
        }
//        }else{
//        dataline ="<TR><TDWIDTH=40%>"+resultrow[j]+" - "+datarow[0]+"</TD>";
//        dataline += "<TD align=middle class=DataGrid>"+datarow[1]+"</TD>";
//        dataline += "<TD WIDTH=40% align=middle>";
//        grade = "<TD WIDTH=40% align=middle>";
//        }
        sql = "select xcode from smstudentcurriculum where xstudentid='"+studentid+"' and xdesc='"+result[i]+"'";
        comparerow = zabinfo.getSqlValues(sql);

        //detailrow[2]="0";
        for(int z = 0; z < comparerow.length; z++){
          if(z==0)
            sql = "select xsemester,xfinalgrade,xrow from smstudentcurriculum where xstudentid='"+studentid+"' and xcode='"+comparerow[z]+"' and xrow>0";
          else
            sql = "select xsemester,xfinalgrade,xrow from smstudentcurriculum where xstudentid='"+studentid+"' and xcode='"+comparerow[z]+"' and xrow>"+zabTools.getInt(detailrow[2])+"";
          detailrow = zabinfo.getSqlRow(sql);
//          dataline += "<TD class=DataGrid WIDTH=40% align=middle>"+datarow[3]+"<BR></TD>";
          if(detailrow[0].equals("")) detailrow[0] = " - ";
          if(detailrow[1].equals("")) detailrow[1] = " - ";
          if(resultrow[j].equals(comparerow[z])){
            dataline += detailrow[0]+"<BR>\n";
            grade += detailrow[1]+"<BR>\n";
          }
        }
        s += dataline+"</TD>"+grade+"</TD></TR>";
//        sql = "select xcode,xlong,xcredit,xsemester,xfinalgrade from smstudentcurriculum where xstudentid='"+studentid+"' and xrow='"+resultrow[j]+"'";
//        datarow = zabinfo.getSqlRow(sql);
//        if(datarow[3].equals("")) datarow[3]=" - ";
//        s +="<TR><TD class=DataGrid WIDTH=40%>"+datarow[0]+" - "+datarow[1]+"</TD><TD align=middle class=DataGrid>"+datarow[2]+"</TD><TD class=DataGrid WIDTH=40% align=middle>"+datarow[3]+"</TD><TD class=DataGrid WIDTH=40% align=middle>"+datarow[4]+"</TD></TR>\n";
      }
//      s += "<TR></TR>";
//      s += "<TR></TR>";
//      s += "<TR></TR>";
//      s += "<TR></TR>";
        s += "</TABLE><TD><TR>\n";
//      s += "</TD></TR></TABLE>";
    }
    java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

    s += "<TR><TD><SPAN style=\"FONT-FAMILY: Tahoma,Verdana, sans-serif; FONT-SIZE: 0.7em; LINE-HEIGHT: 1.58em\">";
    s += "Printed By "+fname+" "+mname+" "+lname;
    s += "</SPAN></TD></TR>";
    s += "<TR><TD><SPAN style=\"FONT-FAMILY: Tahoma,Verdana, sans-serif; FONT-SIZE: 0.7em; LINE-HEIGHT: 1.58em\">";
    s += "Print Time "+zabTools.dateDisplay(today.toString(),'D','-') ;
    s += "</SPAN></TD></TR>";
    s += "</TABLE>\n";
    return s;
   }


  public static String GradeReportBySemester(zabInfo zabinfo){
    String s = "";
    String sql = "";
    String [] result = null;
    String [] datarow = null;
    String [] resultrow = null;
    String degree = ""+zabinfo.getLookupValue("smstudent.xdegree(xstudentid = '"+studentid+"')");
    String department = ""+zabinfo.getLookupValue("smstudent.xdepartment(xstudentid = '"+studentid+"')");


    sql = "select distinct xsemester from smstudentcurriculum where xstudentid='"+studentid+"' and xsemester<>'' order by xsemester asc";
    result = zabinfo.getSqlValues(sql);

    if(result == null) return "";

//    s += "<TABLE style=\"POSITION:relative;top:20; BACKGROUND: url(images/background.jpg)\" cellspacing=0 cellpadding=4 align=center BORDER=0 WIDTH=98% >\n";
    s += "<TABLE style=\"POSITION:relative;top:20; \" cellspacing=0 cellpadding=4 align=center BORDER=0 WIDTH=98% >\n";

    s += "<TR><TD>\n";
                 //
    s += "<TABLE BORDER=0 cellspacing=0 cellpadding=0 WIDTH=100%>\n";
    s += "<TR><TD align=center>";
    s += "<SPAN STYLE=\"FONT-FAMILY: Verdana, sans-serif; FONT-SIZE:1.5em;FONT-WEIGHT:BOLD;\">UNITED  INTERNATIONAL  UNIVERSITY</SPAN>";
    s += "</TD></TR>";
    s += "<TR><TD align=center>";
    s += "<SPAN STYLE=\"FONT-FAMILY: Verdana, sans-serif; FONT-SIZE:1.0em;FONT-WEIGHT:BOLD;\">Grade Report By Semester</SPAN>";
    s += "</TD></TR>";
    s += "</TABLE></TD></TR>";

    s += "<TR><TD>\n";
                 //cellspacing=0 cellpadding=0
    s += "<TABLE BORDER=0 WIDTH=100%>\n";
    s += "<TR><TD align=left WIDTH=50%>";
                                                                                                   //cellspacing=2 cellpadding=0
    s += "<TABLE align=center style=\"FONT-FAMILY: Verdana, sans-serif; FONT-SIZE: 0.7em; LINE-HEIGHT: 1.58em\" BORDER=0 WIDTH=100%>\n";

    s += "<TR>\n";
    s += "<TD>Student id</TD><TD>"+studentid+"</TD>";
    s += "</TR>\n";
    s += "<TR>\n";
    s += "<TD>Student Name</TD><TD>"+fname+" "+mname+" "+lname+"</TD>\n";
    s += "</TR>\n";
    s += "<TR>\n";
    s += "<TD>Credit(s) Completed </TD><TD>"+zabinfo.getSummaryValue("smstudentcurriculum", "xcredit", "xstudentid = '"+studentid+"'", "sum").toString()+"</TD>";
    s += "</TR>\n";
    s += "<TR>\n";
    s += "<TD>CGPA</TD><TD>"+cgpa+"</TD>\n";

    s += "</TR>\n";
    s += "</TABLE></TD>\n";

    s += "<TD valign=top align=right WIDTH=50%>";                                        //          cellspacing=2 cellpadding=0

    s += "<TABLE style=\"FONT-FAMILY: Verdana, sans-serif; FONT-SIZE: 0.7em; LINE-HEIGHT: 1.58em\" BORDER=0 WIDTH=60%>\n";
    s += "<TR>";

    s += "<TD align=right >Department</TD><TD align=right >"+department+", "+addsemester+"</TD>";
    s += "</TR>";
    s += "</TABLE></TD>";

    s += "</TR>";
    s += "</TABLE></TD></TR>";

    for(int i = 0; i <result.length;i++){

//      s += "<TR><TD>";
//      s += "<TABLE BORDER=0 class=regcourse WIDTH=100%>\n";
      s += "<TR><TD class=regcourse>"+result[i]+"</TD></TR>\n";
//      s += "<TR></TR>";
//      s += "</TABLE>\n";
      s += "<TR><TD>";

      s += "<TABLE BORDER=0  cellspacing=0 cellpadding=2 style=\"POSITION:relative;LEFT:0;TOP:0;FONT-FAMILY: Arial,Verdana, sans-serif; FONT-WEIGHT:BOLD; FONT-SIZE: 0.7em;\" WIDTH=100%>\n";
      s += "<TH class=regth>Course</TH><TH class=regth>Credit</TH><TH class=regth>Final Grade</TH>\n";

      sql = "select xrow from smstudentcurriculum where xstudentid='"+studentid+"' and xsemester='"+result[i]+"'";
      resultrow = zabinfo.getSqlValues(sql);
      if(resultrow ==null)
        return "";
      for(int j = 0; j <resultrow.length;j++){
        sql = "select xcode,xlong,xcredit,xfinalgrade from smstudentcurriculum where xstudentid='"+studentid+"' and xrow='"+resultrow[j]+"'";
        datarow = zabinfo.getSqlRow(sql);
        if(datarow[3].equals("")) datarow[3]=" - ";
        if(j == resultrow.length-1){
          s += "<TR><TD class=DataGrid   style=\"BORDER-BOTTOM: darkgray 1px solid;\" WIDTH=60%>"+datarow[0]+" - "+datarow[1]+"</TD>";
          s += "<TD align=middle class=DataGrid   style=\"BORDER-BOTTOM: darkgray 1px solid;\" >"+datarow[2]+"</TD>";
          s += "<TD class=DataGrid align=middle  style=\"BORDER-BOTTOM: darkgray 1px solid;BORDER-RIGHT: darkgray 1px solid;\" >"+datarow[3]+"</TD>";
          s += "</TR>\n";
        }else{
          s += "<TR><TD class=DataGrid WIDTH=60%>"+datarow[0]+" - "+datarow[1]+"</TD>";
          s += "<TD align=middle class=DataGrid>"+datarow[2]+"</TD>";
          s += "<TD class=DataGrid align=middle style=\"BORDER-RIGHT: darkgray 1px solid;>"+datarow[3]+"</TD>";
          s += "</TR>\n";
        }
      }
//      s += "<TR></TR>";
//      s += "<TR></TR>";
//      s += "<TR></TR>";
//      s += "<TR></TR>";
//      s += "<TR></TR>";
      s += "</TABLE></TD></TR>\n";
    }

    java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

    s += "<TR><TD><SPAN style=\"FONT-FAMILY: Tahoma,Verdana, sans-serif; FONT-SIZE: 0.7em; LINE-HEIGHT: 1.58em\">";
    s += "Printed By "+fname+" "+mname+" "+lname;
    s += "</SPAN></TD></TR>";
    s += "<TR><TD><SPAN style=\"FONT-FAMILY: Tahoma,Verdana, sans-serif; FONT-SIZE: 0.7em; LINE-HEIGHT: 1.58em\">";
    s += "Print Time "+zabTools.dateDisplay(today.toString(),'D','-') ;
    s += "</SPAN></TD></TR>";
    s += "</TABLE>";
    return s;
   }

   public static String ScheduleMain(zabInfo zabinfo,java.sql.Date schdate){
    String s = "";
    String sql = "";
    String [] result = null;
    String [] datarow = new String[12];
    String [] detailrow = null;
    String [] classheader = null;
    String date = "";
    String classid = "";
    String year = "";
//    String date = "";
    String daysname = "";
    date = zabinfo.req.getParameter("currentdate");
    java.sql.Date today = null;
//    if(date == null)
      today = schdate;// new java.sql.Date(System.currentTimeMillis());
//    else
//      today = zabTools.getDate(date);

    year = today.toString();
    date = today.toString();
    year= year.substring(0,4)+zabTools.padL(year.substring(5,7),2,'0')+zabTools.padL(year.substring(8,10),2,'0');

    sql = "select xdesc,xdaysname,xmonthname from smcalender where xsemester='"+semester+"' and xyearperdate="+zabTools.getInt(year)+"";
    result = zabinfo.getSqlRow(sql);

    int count = 0;

        s += "<FORM onSubmit=\"return submitIt(this)\" ACTION=\""+zabinfo.servlet+"\" METHOD=POST name=\"login\">\n";
        s += "<INPUT class=p NAME=screen TYPE=hidden VALUE=\"*smstudentmain\">\n";
        s += "<TABLE align=center id=title style=\"POSITION:relative;top:20;\" BORDER=0 cellspacing=0 cellpadding=2 width=98% height=150px>\n";
        s += "<TR>\n";
        if(result == null){
          today = new java.sql.Date(System.currentTimeMillis());
          date = today.toString();
          s += "<TD align=center><SPAN STYLE=\"FONT-FAMILY: Arial,Verdana, sans-serif; FONT-WEIGHT:BOLD; FONT-SIZE: 0.8em;\">Class Schedule for "+date.substring(8,10)+","+date.substring(5,7)+","+date.substring(0,4)+"</SPAN>";
        }else
          s += "<TD align=center><SPAN STYLE=\"FONT-FAMILY: Arial,Verdana, sans-serif; FONT-WEIGHT:BOLD; FONT-SIZE: 0.8em;\">Class Schedule for "+result[1]+", "+result[2]+"  "+date.substring(8,10)+","+date.substring(0,4)+"</SPAN>";
        s += "</TD></TR>\n";
        s += "<TR>\n";
        s += "<TD>\n";

        s += "<TABLE BORDER=0 align=center cellspacing=0 cellpadding=1 WIDTH=99%>\n";
        s += "<TR><TD align=left>";
//        s += "<A href=\""+zabConf.servletRoot+"/login?screen=*smstudentmain?CurrentDate=1%2f23%2f2006\">&lt;Yesterday</A></TD><TD align=right><A href=#>Tomorrow&gt;</A></TD>";
        s += "<INPUT NAME=command TYPE=submit class=schbutton value=&lt;Yesterday></TD>";
        s += "<TD align=right><input name=command type=submit  class=schbutton value=Tomorrow&gt;></TD>";
        s += "</TR></TABLE></TD></TR>";

        s += "<TR>\n";
        s += "<TD>\n";

        s += "<TABLE BORDER=0 align=center valign=top cellspacing=0 cellpadding=3 WIDTH=99%>\n";
        s += "<TR>";
        s += "<TD class=regth align=middle>Time</TD><TD class=regth align=middle>Course</TD>\n";
        s += "<TD class=regth align=middle>Section</TD><TD class=regth align=middle>Room</TD><TD class=regth align=middle>Course Teacher</TD>\n";
        s += "</TR>";
        s += "</FORM>";

    if(result == null){
          s += "<TR>";
          s += "<TD class=DataGrid align=center WIDTH=150px></TD>";
          s += "<TD class=DataGrid ></TD>";
          s += "<TD class=DataGrid align=center WIDTH=50px></TD>";
          s += "<TD class=DataGrid align=center WIDTH=50px></TD>";
          s += "<TD class=DataGridRight></TD>";
          s += "</TR>";
    }
    if(result !=null){
      daysname = result[1];
      int index = result[0].indexOf("Classes");
      if(index<0){

/*        s += "<FORM onSubmit=\"return submitIt(this)\" ACTION=\""+zabinfo.servlet+"\" METHOD=POST name=\"login\">\n";
        s += "<INPUT class=p NAME=screen TYPE=hidden VALUE=\"*smstudentmain\">\n";
        s += "<TABLE align=center id=title style=\"POSITION:relative;top:20;\" BORDER=0 cellspacing=0 cellpadding=2 width=98% height=150px>\n";
        s += "<TR>\n";
        s += "<TD align=center><SPAN STYLE=\"FONT-FAMILY: Arial,Verdana, sans-serif; FONT-WEIGHT:BOLD; FONT-SIZE: 0.8em;\">Class Schedule for "+result[1]+", "+result[2]+"  "+date.substring(8,10)+","+date.substring(0,4)+"</SPAN>";
        s += "</TD></TR>\n";
        s += "<TR>\n";
        s += "<TD>\n";

        s += "<TABLE BORDER=0 align=center cellspacing=0 cellpadding=1 WIDTH=99%>\n";
        s += "<TR><TD align=left>";
//        s += "<A href=\""+zabConf.servletRoot+"/login?screen=*smstudentmain?CurrentDate=1%2f23%2f2006\">&lt;Yesterday</A></TD><TD align=right><A href=#>Tomorrow&gt;</A></TD>";
        s += "<INPUT NAME=command TYPE=submit style=\"border-bottom:0px;border-top:0px;border-right:0px;border-left:0px\" value=&lt;Yesterday></TD>";
        s += "<TD align=right><input name=command type=submit  style=\"border-bottom:0px;border-top:0px;border-right:0px;border-left:0px\" value=Tomorrow&gt;></TD>";
        s += "</TR></TABLE></TD></TR>";

        s += "<TR>\n";
        s += "<TD>\n";

        s += "<TABLE BORDER=0 align=center valign=top cellspacing=0 cellpadding=3 WIDTH=99%>\n";
        s += "<TR>";
        s += "<TD class=regth align=middle>Time</TD><TD class=regth align=middle>Course</TD>\n";
        s += "<TD class=regth align=middle>Section</TD><TD class=regth align=middle>Room</TD><TD class=regth align=middle>Course Teacher</TD>\n";
        s += "</TR>";
        s += "</FORM>";
 */
//       schdate= zabTools.getDate("2006-01-23");

      // sql ="select
      }
    }

    sql = "select xclassid from smstudentcurriculum where xsemester='"+semester+"' and xstudentid='"+studentid+"'";
    result = zabinfo.getSqlValues(sql);

//    sql = "select  from smstudentcurriculum where xsemester='"+semester+"' and xstudentid='"+studentid+"'";
//    result = zabinfo.getSqlValues(sql);
    if(result !=null){
    for(int i =0;i<result.length;i++){
      sql = "select xday from smclassdetail where xclassid='"+result[i]+"'";
      datarow = zabinfo.getSqlValues(sql);

      for(int j =0;j<datarow.length;j++){
        sql = "select xtime,xclasstype,xroom from smclassdetail where xclassid='"+result[i]+"' and xday='"+datarow[j]+"'";
        detailrow = zabinfo.getSqlRow(sql);

        sql = "select xsec,xstaff from smclassheader where xclassid='"+result[i]+"'";
        classheader = zabinfo.getSqlRow(sql);

//        if((classheader[1].equals(null)) || (classheader[1].equals(""))) classheader[1] = " - ";
        if(daysname.equals(datarow[j])){
          count = count+1;

          s += "<TR>";
//        if(j == datarow.length-1){
//          s += "<TD class=DataGrid style=\"BORDER-BOTTOM: darkgray 1px solid\" align=center WIDTH=150px>"+detailrow[0]+"</TD>";
//          s += "<TD class=DataGrid style=\"BORDER-BOTTOM: darkgray 1px solid\" >"+zabinfo.getLookupValue("smstudentcurriculum.xlong(xstudentid = '"+studentid+"' and xsemester='"+semester+"' and xclassid='"+result[i]+"')")+"</TD>";
//          s += "<TD class=DataGrid style=\"BORDER-BOTTOM: darkgray 1px solid\" align=center WIDTH=50px>"+classheader[0]+"</TD>";
//          s += "<TD class=DataGrid style=\"BORDER-BOTTOM: darkgray 1px solid;BORDER-RIGHT: darkgray 1px solid\" >"+zabinfo.getLookupValue("pdmst.xname(xstaff = '"+classheader[1]+"')")+"</TD>";
//        }else{
          s += "<TD class=DataGrid align=center WIDTH=150px>"+detailrow[0]+"</TD>";
          s += "<TD class=DataGrid >"+zabinfo.getLookupValue("smstudentcurriculum.xlong(xstudentid = '"+studentid+"' and xsemester='"+semester+"' and xclassid='"+result[i]+"')")+"</TD>";
          s += "<TD class=DataGrid align=center WIDTH=50px>"+classheader[0]+"</TD>";
          s += "<TD class=DataGrid align=center WIDTH=50px>"+detailrow[2]+"</TD>";
          if(!classheader[1].equals(""))
            s += "<TD class=DataGridRight>"+zabinfo.getLookupValue("pdmst.xname(xstaff = '"+classheader[1]+"')")+"</TD>";
          else
            s += "<TD class=DataGridRight> - </TD>";
//        }
          s += "</TR>";
        }
      }
    }
    }
    s += "</TABLE>";
    s += "</TD></TR></TABLE>";
                                                                                                                              //
        s += "<TABLE align=center id=title style=\"POSITION:relative;top:10;\" BORDER=0 cellspacing=0 cellpadding=2 width=98% height=200px>\n";
        s += "<TR>\n";
        s += "<TD align=center><SPAN STYLE=\"FONT-FAMILY: Arial,Verdana, sans-serif; FONT-WEIGHT:BOLD; FONT-SIZE: 0.8em;\">Course List for Semester: "+semester+"</SPAN>";
        s += "</TD></TR>\n";
        s += "<TR>\n";
        s += "<TD>\n";

        s += "<TABLE BORDER=0 align=center cellspacing=0 cellpadding=3 WIDTH=99%>\n";
        s += "<TR>";
        s += "<TD class=regth align=middle>Courses</TD><TD class=regth align=middle>Course Teacher</TD>\n";
        s += "<TD class=regth align=middle>Grade</TD>\n";
        s += "</TR>";
    if(result !=null){
    for(int i =0;i<result.length;i++){
      sql = "select xlong,xfinalgrade from smstudentcurriculum where xstudentid = '"+studentid+"' and xsemester='"+semester+"' and xclassid='"+result[i]+"'";
      detailrow = zabinfo.getSqlRow(sql);
      if(detailrow[1].equals("")) detailrow[1]="-";
      sql = "select xsec,xstaff from smclassheader where xclassid='"+result[i]+"'";
      datarow = zabinfo.getSqlRow(sql);

      s += "<TR>";

      if(i == result.length-1){
        s += "<TD class=DataGrid style=\"BORDER-BOTTOM: darkgray 1px solid\" >"+detailrow[0]+" ["+datarow[0]+"]</TD>";
        if(datarow[1].equals(""))
          s += "<TD class=DataGrid  style=\"BORDER-BOTTOM: darkgray 1px solid\" > - </TD>";
        else
          s += "<TD class=DataGrid  style=\"BORDER-BOTTOM: darkgray 1px solid\" >"+zabinfo.getLookupValue("pdmst.xname(xstaff = '"+datarow[1]+"')")+"</TD>";
          s += "<TD class=DataGrid style=\"BORDER-BOTTOM: darkgray 1px solid;BORDER-RIGHT: darkgray 1px solid\"  align=center WIDTH=50px>"+detailrow[1]+"</TD>";
      }else{
        s += "<TD class=DataGrid >"+detailrow[0]+" ["+datarow[0]+"]</TD>";
        if(datarow[1].equals(""))
          s += "<TD class=DataGrid > - </TD>";
        else
          s += "<TD class=DataGrid >"+zabinfo.getLookupValue("pdmst.xname(xstaff = '"+datarow[1]+"')")+"</TD>";
          s += "<TD class=DataGridRight align=center WIDTH=50px>"+detailrow[1]+"</TD>";
      }
        s += "</TR>";
    }
    }
    s += "</TABLE>";
    s += "</TD></TR></TABLE>";
    smdata=zabinfo;
    return s;
   }


   public static String StudentSchedule(zabInfo zabinfo){
    String s = "";
    String sql = "";
    String [] result = null;
    String [] datarow = new String[12];
    String [] detailrow = null;
    String date = "";
    String classid = "";
    int year = 0;
    sql = "select year(xdate) from smcalender where xsemester='"+semester+"'";
    result = zabinfo.getSqlRow(sql);

    if(result !=null)
      year = zabTools.getInt(result[0]);

    String [] month = {"January","February","March","April","May","June","July","August","September","October","November","December"};

    //main table          //
    s += "<TABLE BORDER=0 style=\"position:relative;top:20\" cellspacing=0 cellpadding=0 WIDTH=100%>\n";
    s += "<TR><TD>";

    s += "<TABLE BORDER=0 cellspacing=0 cellpadding=0 WIDTH=100%>\n";
    s += "<TR>";

    int count = 0;

    sql = "select distinct xmonthname from smcalender where xsemester='"+semester+"' order by xmonthname";
    result = zabinfo.getSqlValues(sql);

    if(result == null)
      return "<FONT SIZE=+2 COLOR=red>Academic Calender is Empty For Semester "+semester+"</FONT>";

    if(result != null){
      for(int i =0;i<result.length;i++){
        for(int j =0;j<month.length;j++){
          if(month[j].equals(result[i])){
            datarow[j]=result[i];
          }
        }
        count = count+1;
      }
    }

    java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
    java.sql.Date curdate = new java.sql.Date(System.currentTimeMillis());

    for(int i =0;i<count;i++){
      s += "<TD class=regth align=middle>"+datarow[i]+"</TD>\n";
    }
    s += "</TR>\n";
    s += "</TABLE>\n";
    s += "</TD></TR>";
    s += "<TR><TD>";
    s += "<TABLE BORDER=0 WIDTH=100%>\n";
    s += "<TR>";
    s += "<TD class=regth align=middle>Saturday</TD><TD class=regth align=middle>Sunday</TD>\n";
    s += "<TD class=regth align=middle>Monday</TD><TD class=regth align=middle>Tuesday</TD>\n";
    s += "<TD class=regth align=middle>Wednesday</TD><TD class=regth align=middle>Thursday</TD>\n";
    s += "<TD class=regth align=middle>Friday</TD>\n";
    s += "</TR>\n";

    sql = "select xyearperdate from smcalender where xsemester='"+semester+"'";
    result = zabinfo.getSqlValues(sql);
    int index = 0;

    if(result == null)
      return "<FONT SIZE=+2 COLOR=red>Academic Calender is Empty For Semester "+semester+"</FONT>";

    for(int i =0;i<result.length;i++){

      sql = "select xdate,xdaysname,xdesc from smcalender where xyearperdate="+zabTools.getInt(result[i])+"";
      datarow = zabinfo.getSqlRow(sql);

      index = datarow[2].indexOf("Classes");
      sql = "select xrow from smstudentcurriculum where xsemester='"+semester+"' and xstudentid='"+studentid+"'";
      detailrow = zabinfo.getSqlValues(sql);

//      for(int j = 0; j<detailrow.length;j++){

      today = zabTools.getDate(datarow[0].substring(0,10));
      datarow[0] = datarow[0].substring(8,10);
      if(i == 0){
        if(datarow[1].equals("Saturday")){
          s += "<TR>";
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Saturday",zabinfo);
          s+= "</TD>\n";
        }else if(datarow[1].equals("Sunday")){
          s += "<TR>";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Sunday",zabinfo);
          s+= "</TD>\n";
        }else if(datarow[1].equals("Monday")){
          s += "<TR>";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(2*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Monday",zabinfo);
          s+= "</TD>\n";
        }else if(datarow[1].equals("Tuesday")){
          s += "<TR>";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(3*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(2*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Tuesday",zabinfo);
          s+= "</TD>\n";
        }else if(datarow[1].equals("Wednesday")){

          s += "<TR>";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(4*24*60*60*1000)).getDate();
          s += "</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(3*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(2*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(24*60*60*1000)).getDate();
          s+= "</TD>\n";
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Wednesday",zabinfo);
          s += "</TD>\n";
        }else if(datarow[1].equals("Thursday")){
          s += "<TR>";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(5*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(4*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(3*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(2*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Thursday",zabinfo);
          s+= "</TD>\n";
        }else if(datarow[1].equals("Friday")){
          s += "<TR>";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(6*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(5*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(4*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(3*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(2*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()-(24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Friday",zabinfo);
          s+= "</TD>\n";
        }
      }else if(i == result.length-1){
        if(datarow[1].equals("Saturday")){
          s += "<TR>";
          s += zabConf.schtd+datarow[0]+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(2*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(3*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(4*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(5*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(6*24*60*60*1000)).getDate();
          if(index >=0)
            s += getClass("Saturday",zabinfo);
          s+= "</TD>\n";
        }else if(datarow[1].equals("Sunday")){
          s += zabConf.schtd+datarow[0]+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(2*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(3*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(4*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(5*24*60*60*1000)).getDate();
          if(index >=0)
            s += getClass("Sunday",zabinfo);
          s+= "</TD>\n";
        }else if(datarow[1].equals("Monday")){
          s += zabConf.schtd+datarow[0]+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(2*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(3*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(4*24*60*60*1000)).getDate();
          if(index >=0)
            s += getClass("Monday",zabinfo);
          s+= "</TD>\n";
        }else if(datarow[1].equals("Tuesday")){
          s += zabConf.schtd+datarow[0]+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(2*24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(3*24*60*60*1000)).getDate();
          if(index >=0)
            s += getClass("Tuesday",zabinfo);
          s+= "</TD>\n";
        }else if(datarow[1].equals("Wednesday")){
          s += zabConf.schtd+datarow[0]+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(24*60*60*1000)).getDate()+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(2*24*60*60*1000)).getDate();
          if(index >=0)
            s += getClass("Wednesday",zabinfo);
          s+= "</TD>\n";
        }else if(datarow[1].equals("Thursday")){
          s += zabConf.schtd+datarow[0]+"</TD>\n";
          s += zabConf.schtd+new java.sql.Date(today.getTime()+(24*60*60*1000)).getDate();
          if(index >=0)
            s += getClass("Thursday",zabinfo);
          s+= "</TD>\n";
        }if(datarow[1].equals("Friday")){
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Friday",zabinfo);
          s+= "</TD>\n";
          s += "</TR>";
        }
      }else{

        if(datarow[1].equals("Saturday")){
          s += "<TR>";
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Saturday",zabinfo);
          s+="</TD>\n";
        }else if(datarow[1].equals("Sunday")){
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Sunday",zabinfo);
          s+="</TD>\n";
        }else if(datarow[1].equals("Monday")){
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Monday",zabinfo);
          s+="</TD>\n";
        }else if(datarow[1].equals("Tuesday")){
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Tuesday",zabinfo);
          s+="</TD>\n";
        }else if(datarow[1].equals("Wednesday")){
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Wednesday",zabinfo);
          s+="</TD>\n";
        }else if(datarow[1].equals("Thursday")){
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Thursday",zabinfo);
          s+="</TD>\n";
        }else if(datarow[1].equals("Friday")){
          s += zabConf.schtd+datarow[0];
          if(index >=0)
            s += getClass("Friday",zabinfo);
          s+="</TD>\n";
          s += "</TR>";
        }
      }
    }
//    }
    s += "</TABLE>\n";
    s += "</TD></TR></TABLE>";



    return s;
   }

   public static String StudentMain(zabInfo zabinfo){

    smdata=zabinfo;
    String s = "";
    String sql = "";
    String [] result = null;
    String [] datarow = new String[12];
    String [] detailrow = null;
    String freg = "";
    String treg = "";
    String lastsemester = "";
    semester = ""+zabinfo.ses.getAttribute("semester");
    studentid = ""+zabinfo.ses.getAttribute("email");
    fname = ""+zabinfo.getLookupValue("smstudent.xfirst(xstudentid = '"+studentid+"')");
    mname = ""+zabinfo.getLookupValue("smstudent.xmiddle(xstudentid = '"+studentid+"')");
    lname = ""+zabinfo.getLookupValue("smstudent.xlast(xstudentid = '"+studentid+"')");
    department = ""+zabinfo.getLookupValue("xcodes.xlong(xtype='Department' and xcode = '"+""+zabinfo.getLookupValue("smstudent.xdepartment(xstudentid = '"+studentid+"')")+"')");
    cgpa = ""+zabinfo.getLookupValue("smstudent.xcgpa(xstudentid = '"+studentid+"')");
    addsemester = ""+zabinfo.getLookupValue("smstudent.xsemester(xstudentid = '"+studentid+"')");


    java.sql.Date curDate = new java.sql.Date(System.currentTimeMillis());
    curDate = zabTools.getDate(curDate.toString().substring(0,10));
    String fprereg = "";
    String tprereg = "";
    String classid = "";

    sql = "select xdate from smcalender where xsemester='"+semester+"' and xdiv='Registration'";
    result = zabinfo.getSqlRow(sql);
    if(result != null)
      freg = result[0];
//    System.err.println(sql);

    sql = "select xdate from smcalender where xsemester='"+semester+"' and xdiv='Registration Expired'";
    result = zabinfo.getSqlRow(sql);
    if(result != null)
      treg = result[0];
//    System.err.println(freg+" "+curDate);

    sql = "select xdate from smcalender where xsemester='"+semester+"' and xdiv='Pre-Registration'";
    result = zabinfo.getSqlRow(sql);
    if(result != null)
      fprereg = result[0];

    sql = "select xdate from smcalender where xsemester='"+semester+"' and xdiv='Pre-Registration Expired'";
    result = zabinfo.getSqlRow(sql);
    if(result != null)
      tprereg = result[0];

    //  System.err.println(curDate.getTime()>=zabTools.getDate(freg.substring(0,10)).getTime());
         //table 1
    s =  "<TABLE style=\"POSITION:relative;TOP:30;left:10;\" BORDER=0 cellspacing=0 cellpadding=0 WIDTH=100%>\n";
    s += "<TR>\n";
    s += "<TD width=40% align=center id=title height=100%>\n";
    s += "<TABLE align=center BORDER=0 cellspacing=0 cellpadding=0 width=70% HEIGHT=100%>\n";
    s += "<TR>\n";
    s += "<TD >\n";                    //                                                     //Grade Report,toolbar=0,location=0,directories=0,status=1,menubar=0,scrollbars=1,resizable=1
    s += "<UL>";
    s += "<LI><A href=\""+zabConf.servletRoot+"/login?screen=*smstudentschedule\" >Class Schedule</A>\n";
    s += "<LI><A href=\"javaScript:openWin('"+zabConf.servletRoot+"/login?screen=*smgradereportbysemester&menu=*none*','GradeReportBySemester')\">Grade Report By Semester</A>\n";
    s += "<LI><A href=\"javaScript:openWin('"+zabConf.servletRoot+"/login?screen=*smgradereportbycurriculum&menu=*none*','GradeReportByCurriculum')\" >Grade Report By Curriculum</A>\n";
    s += "<LI><A href=\"javaScript:openWin('"+zabConf.servletRoot+"/login?screen=*smstudentcurriculum&menu=*none*','StudentCurriculum')\" >Student Curriculum</A>\n";
//temp    if(curDate.getTime()>=zabTools.getDate(fprereg.substring(0,10)).getTime() && curDate.getTime()<=zabTools.getDate(tprereg.substring(0,10)).getTime())
      s += "<LI><A href=\""+zabConf.servletRoot+"/login?screen=*smstudentprereg\" >Student Pre-Registration</A>\n";
//temp    else
      s += "<LI disabled><A href=\"#\" >Student Pre-Registration</A>\n";
//temp    if(curDate.getTime()>=zabTools.getDate(freg.substring(0,10)).getTime() && curDate.getTime()<=zabTools.getDate(treg.substring(0,10)).getTime())
      s += "<LI><A href=\""+zabConf.servletRoot+"/login?screen=*smstudentreg\" >Student Registration</A>\n";
//temp    else
      s += "<LI disabled><A href=\"#\" >Student Registration</A>\n";
    s += "</LI></UL>";
    s += "</TD></TR></TABLE>\n";
/*          //table 2
    s += "<TABLE align=center id=title BORDER=10 cellspacing=0 cellpadding=0 width=100% HEIGHT=100%>\n";
    s += "<TR>\n";
    s += "<TD >\n";                    //                                                     //Grade Report,toolbar=0,location=0,directories=0,status=1,menubar=0,scrollbars=1,resizable=1
    s += "<A href=\"javaScript:openWin('"+zabConf.servletRoot+"/login?screen=*smgradereportbysemester&menu=*none*','GradeReportBySemester')\">Grade Report By Semester</A>\n";
//    s += "<A href=\"javascript:getReport()\" >Grade Report By Semester</A>\n";
    s += "</TD>\n";
    s += "</TR>\n";
    s += "<TR>\n";
    s += "<TD >\n";
    s += "<A href=\"javaScript:openWin('"+zabConf.servletRoot+"/login?screen=*smgradereportbycurriculum&menu=*none*','GradeReportByCurriculum')\" >Grade Report By Curriculum</A>\n";
    s += "</TD>\n";
    s += "</TR>\n";
    s += "<TR>\n";
    s += "<TD >\n";
    s += "<A href=\"javaScript:openWin('"+zabConf.servletRoot+"/login?screen=*smstudentcurriculum&menu=*none*','StudentCurriculum')\" >Student Curriculum</A>\n";
    s += "</TD>\n";
    s += "</TR>\n";

    if(curDate.getTime()>=zabTools.getDate(fprereg.substring(0,10)).getTime() && curDate.getTime()<=zabTools.getDate(tprereg.substring(0,10)).getTime()){
      s += "<TR>\n";
      s += "<TD >\n";
      s += "<A href=\""+zabConf.servletRoot+"/login?screen=*smstudentprereg\" >Student Pre-Registration</A>\n";
      s += "</TD>\n";
      s += "</TR>\n";
    }else{
      s += "<TR>\n";
      s += "<TD disabled>\n";
      s += "Student Pre-Registration\n";
      s += "</TD>\n";
      s += "</TR>\n";
    }
    if(curDate.getTime()>=zabTools.getDate(freg.substring(0,10)).getTime() && curDate.getTime()<=zabTools.getDate(treg.substring(0,10)).getTime()){
      s += "<TR>\n";
      s += "<TD >\n";
      s += "<A href=\""+zabConf.servletRoot+"/login?screen=*smstudentreg\" >Student Registration</A>\n";
      s += "</TD>\n";
      s += "</TR>\n";
    }else{
      s += "<TR>\n";
      s += "<TD disabled>\n";
      s += "Student Registration\n";
      s += "</TD>\n";
      s += "</TR>\n";
    }
    s += "</TABLE>\n";    
*/          //end table 2
    s += "</TD>\n";

    s += "<TD align=center width=60%>\n";
          //table 3
    s += "<TABLE align=center id=title class=menulist BORDER=0 cellspacing=2 cellpadding=2 width=90%  height=100%>\n";
    s += "<TR>\n";
    s += "<TD>\n";
         // table 4
    s += "<TABLE BORDER=0 class=student cellspacing=2 cellpadding=2  height=100%>\n";

    s += "<TR>\n";
    s += "<TD><SPAN class=student><B>Name:</B> "+fname+" "+mname+" "+lname+"&nbsp;&nbsp;&nbsp;</SPAN>";
    s += "</TD>\n";
    s += "<TR>\n";
    s += "<TD><B>Department:</B> "+department+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    s += "</TD>\n";
    s += "<TR>\n";
    s += "<TD><B>Semester: </B></TD>\n";

    s += "<TR>\n";
    s += "<TD>\n";
    s += "<SELECT NAME=xsemester>\n";
    sql = "select distinct xsemester from smstudentcurriculum where xstudentid='"+studentid+"' and xsemester<>''";
    result = zabinfo.getSqlValues(sql);
    for(int i =0;i<result.length;i++){
      if(semester.equals(result[i])){
        lastsemester = ""+zabinfo.getLookupValue("xcodes.xlong(xtype='Semester' and xcode = '"+result[i]+"')");
        s += "<OPTION VALUE="+result[i]+" SELECTED>"+result[i]+"</OPTION>\n";
      }else
        s += "<OPTION VALUE="+result[i]+">"+result[i]+"</OPTION>\n";
    }

    s += "</SELECT>\n";
    s += "&nbsp;&nbsp;&nbsp;"+lastsemester;
    s += "</TD>\n";
//    s += "<TD>\n";
//    s += "</TD>\n";
//    s += "      </TD>\n";
    s += "</TR>\n";
    s += "<TR>\n";
    s += "<TD><B>CGPA:</B> "+cgpa+"</TD>\n";
    s += "</TR>\n";
    s += "<TR>\n";
    s += "<TD><B>Total Credit:</B> "+zabinfo.getSummaryValue("smstudentcurriculum", "xcredit", "xstudentid = '"+studentid+"'", "sum").toString()+"&nbsp;&nbsp;&nbsp;";
    s += "Done: "+zabinfo.getLookupValue("smstudent.xcredit(xstudentid = '"+studentid+"')")+"</TD>\n";
    s += "</TR></TABLE></TD>\n";
         //end table 4
    s += "<TD><IMG border=0 SRC=\"images/"+studentid+".jpg\" HEIGHT=100px WIDTH=100px>";
    s += "</TD>\n</TR>\n</TABLE>\n";
          // end table 3
    s += "</TD>\n</TR>\n";
//    s += "</td><td></td><td align=right width=100%><img src=\"images/nav_bottom.gif\" height=200>";

    s += "</TABLE>\n";

    //class schedule
    s += ScheduleMain(zabinfo,schdate);

    //class schedule
//    s += ClassSchedule(zabinfo);

    return s;
   }

    public static String insertIntoAR(zabInfo zabinfo){
      String comtype=zabinfo.req.getParameter("Payment Type");
      String voucher = zabinfo.getTrn(id,"AR Transactions","SV--",10);
      String date = "";
      String dateexp = "";
      String cursemester = (String)zabinfo.ses.getAttribute("semester");
      int yearperdate = 0;
     // string [] resultexp = null;

      //*********1st installment date************
      sql = "select xdate from smcalender where xsemester = '"+cursemester+"' and xdocnum='1st Installment'";
      result = zabinfo.getSqlRow(sql);
      if(result != null){
        date = result[0].substring(0,10);
      }

      int year = zabTools.getInt(date.substring(0,4));
      int per = zabTools.getInt(date.substring(5,7));
//      String docnum = "";

//      yearperdate = zabTools.getInt(year+zabTools.padL(""+per,2,'0')+zabTools.padL(date.substring(8,10),2,'0'));
//      docnum = ""+zabinfo.getLookupValue("smcalender.xdocnum(xyearperdate='"+yearperdate+"')");

      //*********1st installment exp date************
      sql = "select xdate from smcalender where xsemester = '"+cursemester+"' and xdocnum='1st Installment Expired'";
      result = zabinfo.getSqlRow(sql);
      if(result != null){
        dateexp = result[0].substring(0,10);
      }
     if(comtype.equals("Cash Payment")){
//System.err.println("bingo"+totalfees);
//        docnum = ""+zabinfo.getLookupValue("smcalender.xdocnum(xyearperdate='"+yearperdate+"')");
        sql = "insert into arhed(ztime,zid,xvoucher,xdate,xdateexp,xcus,xorg,";
        sql += "xref,xcur,xexch,xprime,xbase,xbalprime,xbalbase,xyear,xper,";
        sql += "xyearper,xstatusjv,xsign,zauserid,xsemester,xdiscprime,xdiscbase)";
        sql += " values('"+new java.sql.Date(System.currentTimeMillis())+"',"+zabTools.getInt(id)+",'"+voucher+"'";
        sql += ",'"+zabTools.getDate(date)+"','"+zabTools.getDate(dateexp)+"','"+studentid+"'";
        sql += ",'"+fname+" "+mname+" "+lname+"'";
        sql += ",'1st Installment','BDT',1,"+totalfees+"";
        sql += ","+totalfees+","+totalfees+","+totalfees+","+year+","+per+","+(year*100)+per+",'Open'";
        sql += ",1,'"+zabinfo.ses.getAttribute("email")+"','"+cursemester+"'";
        sql += ",0,0)";
        zabinfo.executeSql(sql);
     }else if(comtype.equals("3 Installments")){
        double totfees = 0;
        double payment = 0;

        // 1st Installment
        totfees = totalfees-tutionfees;

        payment = firstinst;//(tutionfees*30/100)+totfees;

        sql = "insert into arhed(ztime,zid,xvoucher,xdate,xdateexp,xcus,xorg,xref,";
        sql += "xcur,xexch,xprime,xbase,xbalprime,xbalbase,xyear,xper,xyearper,";
        sql += "xstatusjv,xsign,zauserid,xsemester,xdiscprime,xdiscbase)";
        sql += " values('"+new java.sql.Date(System.currentTimeMillis())+"',"+zabTools.getInt(id)+",'"+voucher+"'";
        sql += ",'"+zabTools.getDate(date)+"','"+zabTools.getDate(dateexp)+"','"+studentid+"'";
        sql += ",'"+fname+" "+mname+" "+lname+"'";
        sql += ",'1st Installment','BDT',1,"+payment+"";
        sql += ","+payment+","+payment+","+payment+","+year+","+per+","+(year*100)+per+",'Open'";
        sql += ",1,'"+zabinfo.ses.getAttribute("email")+"','"+cursemester+"'";
        sql += ",0,0)";
        zabinfo.executeSql(sql);

        //2nd Installment

        voucher = zabinfo.getTrn(id,"AR Transactions","SV--",10);
        sql = "select xdate from smcalender where xsemester = '"+cursemester+"' and xdocnum='2nd Installment'";
        result = zabinfo.getSqlRow(sql);
        if(result != null){
          date = result[0].substring(0,10);
        }

        year = zabTools.getInt(date.substring(0,4));
        per = zabTools.getInt(date.substring(5,7));

//        yearperdate =  zabTools.getInt(year+zabTools.padL(""+per,2,'0')+zabTools.padL(date.substring(8,10),2,'0'));
//        docnum = ""+zabinfo.getLookupValue("smcalender.xdocnum(xyearperdate='"+yearperdate+"')");

      //*********1st installment exp date************
      sql = "select xdate from smcalender where xsemester = '"+cursemester+"' and xdocnum='2nd Installment Expired'";
      result = zabinfo.getSqlRow(sql);
      if(result != null){
        dateexp = result[0].substring(0,10);
      }
        totfees = tutionfees-(tutionfees*30/100);
        payment = secinst;//(totfees*30/100);

        sql = "insert into arhed(ztime,zid,xvoucher,xdate,xdateexp,xcus,xorg,xref,xcur,";
        sql += "xexch,xprime,xbase,xbalprime,xbalbase,xyear,xper,xyearper,xstatusjv,";
        sql += "xsign,zauserid,xsemester,xdiscprime,xdiscbase)";
        sql += " values('"+new java.sql.Date(System.currentTimeMillis())+"',"+zabTools.getInt(id)+",'"+voucher+"'";
        sql += ",'"+zabTools.getDate(date)+"','"+zabTools.getDate(dateexp)+"','"+studentid+"'";
        sql += ",'"+fname+" "+mname+" "+lname+"'";
        sql += ",'2nd Installment','BDT',1,"+payment+"";
        sql += ","+payment+","+payment+","+payment+","+year+","+per+","+(year*100)+per+",'Open'";
        sql += ",1,'"+zabinfo.ses.getAttribute("email")+"','"+cursemester+"'";
        sql += ",0,0)";
        zabinfo.executeSql(sql);

        //3rd Installment

        voucher = zabinfo.getTrn(id,"AR Transactions","SV--",10);
        sql = "select xdate from smcalender where xsemester = '"+cursemester+"' and xdocnum='3rd Installment'";
        result = zabinfo.getSqlRow(sql);
        if(result != null){
          date = result[0].substring(0,10);
        }

        year = zabTools.getInt(date.substring(0,4));
        per = zabTools.getInt(date.substring(5,7));

//        yearperdate = zabTools.getInt(year+zabTools.padL(""+per,2,'0')+zabTools.padL(date.substring(8,10),2,'0'));
//        docnum = ""+zabinfo.getLookupValue("smcalender.xdocnum(xyearperdate='"+yearperdate+"')");

         //*********1st installment exp date************
      sql = "select xdate from smcalender where xsemester = '"+cursemester+"' and xdocnum='1st Installment Expired'";
      result = zabinfo.getSqlRow(sql);
      if(result != null){
        dateexp = result[0].substring(0,10);
      }
     payment = thirdinst;//totfees-payment;

        sql = "insert into arhed(ztime,zid,xvoucher,";
        sql += "xdate,xdateexp,xcus,xorg,xref,xcur,xexch,xprime,xbase,xbalprime,";
        sql += "xbalbase,xyear,xper,xyearper,xstatusjv,xsign,zauserid,xsemester,xdiscprime,xdiscbase)";
        sql += " values('"+new java.sql.Date(System.currentTimeMillis())+"',"+zabTools.getInt(id)+",'"+voucher+"'";
        sql += ",'"+zabTools.getDate(date)+"','"+zabTools.getDate(dateexp)+"','"+studentid+"'";
        sql += ",'"+fname+" "+mname+" "+lname+"'";
        sql += ",'3rd Installment','BDT',1,"+payment+"";
        sql += ","+payment+","+payment+","+payment+","+year+","+per+","+(year*100)+per+",'Open'";
        sql += ",1,'"+zabinfo.ses.getAttribute("email")+"','"+cursemester+"'";
        sql += ",0,0)";
        zabinfo.executeSql(sql);

     }
     return "";
   }

   public static String getClass(String day, zabInfo zabinfo){
    String s = "";
    String sql = "";
    String [] result = null;
    String [] datarow = null;
    String [] detailrow = null;
    String course = "";
    String section = "";


    s = "<TABLE BORDER=0><TR><TD align=middle valign=middle>\n";
    s += "<TABLE BORDER=0 WIDTH=100%>\n";

    sql = "select xclassid from smstudentcurriculum where xsemester='"+semester+"' and xstudentid='"+studentid+"'";
    result = zabinfo.getSqlValues(sql);

    for(int i = 0; i < result.length;i++){

      sql = "select xrow from smclassdetail where xclassid='"+result[i]+"' and xday='"+day+"'";
      detailrow = zabinfo.getSqlValues(sql);

//      if(detailrow == null){}

      if(detailrow != null){
        for(int j = 0; j < detailrow.length;j++){
          course = ""+zabinfo.getLookupValue("smstudentcurriculum.xlong(xclassid = '"+result[i]+"' and xsemester='"+semester+"' and xstudentid='"+studentid+"')");
          section = ""+zabinfo.getLookupValue("smclassheader.xsec(xclassid = '"+result[i]+"')");

          sql = "select xtime,xroom from smclassdetail where xclassid='"+result[i]+"' and xrow='"+detailrow[j]+"'";
          datarow = zabinfo.getSqlRow(sql);

          s +="<TR><TD align=middle class=regcourse>"+course+" ["+section+"] - "+datarow[1]+"</TD></TR>\n";
          s +="<TR><TD align=middle class=regcourse>"+datarow[0]+"</TD></TR>\n";
        }
      }

    }
      s += "</TABLE>\n";
      s += "</TD></TR></TABLE>\n";

    return s;
   }
}

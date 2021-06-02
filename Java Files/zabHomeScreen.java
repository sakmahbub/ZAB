package zaberp.zab;

import java.sql.*;
import java.util.*;
import java.io.*;
import java.math.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class zabHomeScreen {

  static final char DQUOTE = '\"';
  static final String SEPvalue = "" + '\u00FF';
  static String id = "";
  static String sql = "";
  static zabInfo smdata = new zabInfo();
  static String [] result = null;
  static String [][] resultrows = null;
  static String [] sa = null;

  public zabHomeScreen() {
  }

  public static String specialPage(zabInfo zabinfo){
//	  String s = "";
	  String homescreen = (String)zabinfo.ses.getAttribute("homescreen");
	  if(homescreen.equals("sales")){
		  return sales(zabinfo);
	  }else if(homescreen.equals("general")){
		  return general(zabinfo);
	  }else if(homescreen.equals("doctor")){
		  return doctor(zabinfo);
	  }else if(homescreen.equals("management")){
		  return management(zabinfo);
	  }else if(homescreen.equals("patient")){
		  return patient(zabinfo);
	  }else if(homescreen.equals("gulmanagement")){
		  return gulmanagement(zabinfo);
	  }else if(homescreen.equals("unimartmanagement")){
		  return unimartmanagement(zabinfo);
	  }
	  else if(homescreen.equals("mahbub")){
		  return mahbub(zabinfo);
	  }
	  return "";
  }
  
  public static String sales(zabInfo zabinfo){
	  String s = "";
	  String caption = "Sales";
	  String name = "Sales";
	  int per = 3;
	  int year = 2020;
	  String id = ""+zabinfo.ses.getAttribute("id");
	  String email = ""+zabinfo.ses.getAttribute("email");
	  String sp = "zabsp_hsDashboard_Sales "+id+",'"+email+"','"+name+"',"+per+","+year;

	  //**** executing stored produre
	  zabinfo.executeSP(sp);

      String cashsales = "";
      String creditsales = "";
      String prvcashsales = "";
      String prvcreditsales = "";
      double cashdiff = 0;
      double creditdiff = 0;
      String dataset1 = "";
      String dataset2 = "";
      String aramt = "";
      String collection = "";
      String catitemdata = "";
      String catitemcashdata = "";
      String catitemcreditdata = "";
      String itemdata = "";
      String itemcashdata = "";
      String itemcreditdata = "";
      String catcusdata = "";
      String catcusamtdata = "";
      String territorydata = "";
      String territorysalesdata = "";
      String spdata = "";
      String spsalesdata = "";
      
     // BigDecimal cashsales = new BigDecimal(0);
      
      sql = "select xcashsales,xcreditsales,xcashdiff,xcreditdiff,xdataset1,xdataset2,xaramt,xcollection,";
      sql += "xcatitemdata,xcatitemcashdata,xcatitemcreditdata,xitemdata,xitemcashdata,xitemcreditdata,";
      sql += " xcatcusdata,xcatcusamtdata,xterritorydata,xterritorysalesdata,xspdata,xspsalesdata from hssales";
      sql += " where zid='"+id+"' and xusers='"+email+"' and xname='"+name+"'";
      sql += "and xper="+per+" and xyear="+year;
   	  result = zabinfo.getSqlRow(sql);
   	  if(result==null){
   		  //cashsales = "";
   	  }else{
   		  cashsales = zabTools.decimalDisplay(zabTools.getBigDecimal(result[0]).toString(),0);
   		  cashsales = cashsales.substring(0,cashsales.indexOf("."));
   		  creditsales = zabTools.decimalDisplay(zabTools.getBigDecimal(result[1]).toString(),0);
   		  creditsales = creditsales.substring(0,creditsales.indexOf("."));
   		  cashdiff = zabTools.getDouble(result[2]);
   		  creditdiff = zabTools.getDouble(result[3]);
   		  dataset1 = result[4];
   		  dataset2 = result[5];
   		  aramt = zabTools.decimalDisplay(zabTools.getBigDecimal(result[6]).toString(),0);
   		  aramt = aramt.substring(0,aramt.indexOf("."));
   		  collection = zabTools.decimalDisplay(zabTools.getBigDecimal(result[7]).toString(),0);
   		  collection = collection.substring(0,collection.indexOf("."));
   		  catitemdata = result[8];
   		  catitemcashdata = result[9];
   		  catitemcreditdata = result[10];
   		  itemdata = result[11];
   		  itemcashdata = result[12];
   		  itemcreditdata = result[13];
   		  catcusdata = result[14];
   		  catcusamtdata = result[15];
   		  territorydata = result[16];
   		  territorysalesdata = result[17];
   		  spdata = result[18];
   		  spsalesdata = result[19];
   	  }
   	  
   	  String prdtable = "";
      prdtable += "	<table id=\"top5prdtable\" class=\"table-bordered\" style=\"width:100%;\"cellpadding=5>\n";
      prdtable += "		<thead>\n";
      prdtable += "			<tr>\n"; //class=\"list-header\"
      prdtable += "				<td style=\"background-color:#ff6384;font-weight:bold\" colspan=4>Top 5 Products of, "+per+","+year+"</td>\n";
      prdtable += "			</tr>\n";
      prdtable += "			<th>Product Name</th><td align=\"right\">Qty</td><td align=\"right\">Avg. Rate</td><td align=\"right\">Amount</td>\n";
      prdtable += "		</thead>\n";
      prdtable += "		<tbody>\n";
      
   	  sql = " select c.xdesc,sum(b.xqtyord),sum(b.xqtyord*b.xrate) from opdoheader a join opdodetail b on a.zid=b.zid and a.xdornum=b.xdornum";
   	  sql += " join caitem c on b.zid=c.zid and b.xitem=c.xitem ";
   	  sql += " where a.zid="+id+" and a.xyear="+year+" and a.xper="+per+" group by c.xdesc";
   	  sql += " order by sum(b.xqtyord*b.xrate) desc"; 
   	  
   	  resultrows = zabinfo.getSqlRows(sql);
   	  
   	  if(resultrows==null){
   	  }else{
   		  for(int i =0;i<resultrows.length;i++) {
//   			  prdtable += "<tr><td>"+resultrows[i][0]+"</td><td>"+resultrows[i][1]+"</td><td align=\"right\">"+zabTools.decimalDisplay(resultrows[i][2],2)+"</td></tr>\n";
   			  prdtable += "<tr><td>"+resultrows[i][0]+"</td><td align=\"right\">"+zabTools.decimalDisplay(resultrows[i][1],2)+"</td><td align=\"right\">"+zabTools.decimalDisplay(""+(zabTools.getDouble(resultrows[i][2]))/(zabTools.getDouble(resultrows[i][1])),2)+"</td><td align=\"right\">"+zabTools.decimalDisplay(resultrows[i][2],2)+"</td></tr>\n";
   			  if(i==4) break;
   		  }
   		  prdtable += "</tbody>";
   		  prdtable += "</table>";
   	  }	  
   	 // System.err.println(sql);
   	 // System.err.println(prdtable);
   	  /*
	  s += "<table id=\"table-cap-filter-graph\" align=\"center\" border=\"10\" cellpadding=\"0\" cellspacing=\"0\" width=\"98%\" height=\"100%\">\n";
	  s = "<tr>\n";
	  s += "<td>Bunty1\n";
	  s += "</td>\n";
	  s += "</tr>\n";
	  s += "</table>\n";
*/
	    s  =   "<table id=\"table-cap-filter-graph\" align=center valign=top border=0 cellpadding=10 cellspacing=10 width=99% height=100%>\n";
	    s +=   "	<tr>\n";
	    s +=   "		<td colspan=2 align=left>\n";
	    s +=   "			<div style=\"height:50px;background-color:#fff;box-shadow:1px 1px 2px rgba(0, 0, 0, 0.4);font-size:20px\">"+caption+"</div>\n";
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n";
	    
	    s +=   "	<tr>\n";
	    /*
	    s +=   "		<td align=left valign=\"top\" width=20% >\n";
	    
	    s +=   "			<div class=\"div-hs-filter\">\n";
	    s +=   "				<table id=\"table1-filter\" align=center border=0 cellpadding=0 cellspacing=0>\n";
	    s +=   "					<tr><td>Bingo</td></tr>\n"; 
	    s +=   "				</table>\n"; 
	    s +=   "			</div>\n";
	    
	    s +=   "		</td>\n";
	    */
	    s +=   "		<td align=left valign=top width=70%>\n";
	    s +=   "			<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	    s +=   "				<tr>\n";

	    /*********************** cash sales *************************************/
	    s +=   "					<td class=\"hs-info-td\">\n";
	    s +=   "						<div class=\"card hs-card-sm\">\n";
	    s +=   "							<div class=\"card-header\" style=\"height:50px;\" >\n";
	    s +=   "								<img class=\"hs-info-img\" src=\""+zabConf.imagePath+"\\icons\\sales.png\"/><div class=\"hs-info-title\">Cash Sales</div>\n"; 
	    s +=   "							</div>\n";
	    s +=   "							<div class=\"card-body hs-info-content\">\n";
	    s +=   "								<div><span style=\"font-size:24px;\">"+cashsales+"</span></div>\n";
	    if(cashdiff<0) {
	    	s +=   "							<br><div style=\"font-size:13px;color:#dc3545;\">Over last month "+(0-cashdiff)+"% <i class=\"fa fa-arrow-down\"></i></div>\n";
	    }else{
	    	s +=   "							<br><div style=\"font-size:13px;color:#32cd32;\">Over last month "+cashdiff+"% <i class=\"fa fa-arrow-up\"></i></div>\n";
	    }
	    s +=   "							</div>\n"; // end of card-body
	    s +=   "						</div>\n"; // end of card
	    s +=   "					</td>\n"; // end of first td class="hs-info-td"
	    /*********************** end of cash sales *************************************/

	    /*********************** credit sales *************************************/
	    s +=   "					<td class=\"hs-info-td\">\n";
	    s +=   "						<div class=\"card hs-card-sm\">\n";
	    s +=   "							<div class=\"card-header\" style=\"height:50px;\" >\n";
	    s +=   "								<img class=\"hs-info-img\" src=\""+zabConf.imagePath+"\\icons\\sales.png\"/><div class=\"hs-info-title\">Credit Sales</div>\n"; 
	    s +=   "							</div>\n";
	    s +=   "							<div class=\"card-body hs-info-content\">\n";
	    s +=   "								<div><span style=\"font-size:24px;\">"+creditsales+"</span></div>\n";
	    if(creditdiff<0) {
	    	s +=   "							<br><div style=\"font-size:13px;color:#dc3545;\">Over last month "+(0-creditdiff)+"% <i class=\"fa fa-arrow-down\"></i></div>\n";
	    }else{
	    	s +=   "							<br><div style=\"font-size:13px;color:#32cd32;\">Over last month "+creditdiff+"%  <i class=\"fa fa-arrow-up\"></i></div>\n";
	    }
	    s +=   "							</div>\n"; // end of card-body
	    s +=   "						</div>\n"; // end of card
	    s +=   "					</td>\n"; // end of first td class="hs-info-td"
	    /*********************** end credit sales *************************************/

	    /*********************** AR Amt(Outstanding) *************************************/
	    s +=   "					<td class=\"hs-info-td\">\n";
	    s +=   "						<div class=\"card hs-card-sm\">\n";
	    s +=   "							<div class=\"card-header\" style=\"height:50px;\" >\n";
	    s +=   "								<img class=\"hs-info-img\" src=\""+zabConf.imagePath+"\\icons\\sales.png\"/><div class=\"hs-info-title\">Outstanding</div>\n"; 
	    s +=   "							</div>\n";
	    s +=   "							<div class=\"card-body hs-info-content\">\n";
	    s +=   "								<div><span style=\"font-size:24px;\">"+aramt+"</span></div>\n";
	    s +=   "							</div>\n"; // end of card-body
	    s +=   "						</div>\n"; // end of card
	    s +=   "					</td>\n"; // end of first td class="hs-info-td"
	    /*********************** end AR Amt *************************************/
	    /*********************** Collection *************************************/
	    s +=   "					<td class=\"hs-info-td\" >\n";
	    s +=   "						<div class=\"card hs-card-sm\">\n";
	    s +=   "							<div class=\"card-header\" style=\"height:50px;\" >\n";
	    s +=   "								<img class=\"hs-info-img\" src=\""+zabConf.imagePath+"\\icons\\sales.png\"/><div class=\"hs-info-title\">Collection</div>\n"; 
	    s +=   "							</div>\n";
	    				//********* card body ******************//
	    s +=   "							<div class=\"card-body hs-info-content\">\n";
	    s +=   "								<div><span style=\"font-size:24px;\">"+collection+"</span></div>\n";
	    s +=   "							</div>\n"; // end of card-body
	    s +=   "						</div>\n"; // end of card
	    s +=   "					</td>\n"; // end of first td class="hs-info-td"
	   
	    /*********************** end Collection *************************************/

	    s +=   "				</tr>\n"; // end of first tr
	    s +=   "				<tr>\n"; // 2nd  tr with graph
	    s +=   "					<td colspan=4 class=\"hs-info-td\">\n";
	    s +=   "						<table id=\"table-hs-2nd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% height=100%>\n";
	    s +=   "							<tr>\n"; 
	    s +=   "								<td width=33%>\n";
	    s +=   "									<div style=\"margin-right:10px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"msales\" ></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "								<td width=34%>\n";
	    s +=   "									<div style=\"margin-right:10px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"catitemsales\" ></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "								<td width=33% valign=top>\n";
	    s +=   "									<div style=\"background-color:#fff;\">\n";
	    s +=   prdtable;
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    s +=   "				<tr>\n"; // 3rd  tr with graph
	    s +=   "					<td colspan=4 class=\"hs-info-td\">\n";
	    
	    //********************** 3rd row ***************************************//
	    
	    s +=   "						<table id=\"table-hs-3rd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% >\n";
	    s +=   "							<tr>\n"; 
	    s +=   "								<td width=30% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"catcussales\" height=255px></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "								<td width=35% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"territorysales\" height=\"220px\"></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "								<td width=35% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"spsales\" height=\"220px\"></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    s +=   "			</td>\n";  // end of 80% td
	    s +=   "		</tr>\n"; 
	    s +=   "	</table>\n"; 
	    s +=   "</td>\n"; // end of 80% td
	    /*
	    s +=   "		<td align=left valign=\"top\" width=30%>\n";
	    //s +=   "			<div class=\"div-hs-filter\">\n";
	    s +=   "				<div class=\"div-hs-filter\" style=\"background-color:#fff;\">\n";
	    s +=   "					<canvas id=\"itemsales\" height=\"700px\"></canvas>\n";
	    s +=   "				</div>\n";
	   // s +=   "				<table id=\"table1-filter\" align=center border=0 cellpadding=0 cellspacing=0>\n";
	    //s +=   "					<tr><td>Bingo</td></tr>\n"; 
	    //s +=   "				</table>\n"; 
	    //s +=   "			</div>\n";
	    s +=   "		</td>\n";
	    */
	    s +=   "	</tr>\n";
	    s +=   "</table>\n";
	    
	    s +=   "			<script>\n";
	    s +=   "var config = {\n";
	    s +=   "		type: 'line',\n";
	    s +=   "		data: {\n";
	    s +=   "			labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Cash Sales',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				data: ["+dataset1+"\n";
	    //s +=   "					randomScalingFactor(),\n";
	    //s +=   "					randomScalingFactor(),\n";
	    //s +=   "					randomScalingFactor(),\n";
	    //s +=   "					randomScalingFactor(),\n";
	    //s +=   "					randomScalingFactor(),\n";
	    //s +=   "					randomScalingFactor(),\n";
	    //s +=   "					randomScalingFactor()\n";
	    s +=   "				],\n";
	    s +=   "				fill: false,\n";
	    s +=   "			}, {\n";
	    s +=   "				label: 'Credit Sales',\n";
	    s +=   "				fill: false,\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				data: ["+dataset2+"\n";
	    //s +=   "					randomScalingFactor(),\n";
	    //s +=   "					randomScalingFactor(),\n";
	    //s +=   "					randomScalingFactor(),\n";
	    //s +=   "					randomScalingFactor(),\n";
	    //s +=   "					randomScalingFactor(),\n";
	    //s +=   "					randomScalingFactor(),\n";
	    //s +=   "					randomScalingFactor()\n";
	    s +=   "				],\n";
	    s +=   "			}]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Month Wise Sales of "+year+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Value'+' (Million)'\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";
	    s +=   "var config1 = {\n";
	    s +=   "		type: 'line',\n";
	    s +=   "		data: {\n";
	    s +=   "			labels: ["+catitemdata+"],\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Cash Sales',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				data: ["+catitemcashdata+"\n";
	    s +=   "				],\n";
	    s +=   "				fill: false,\n";
	    s +=   "			}, {\n";
	    s +=   "				label: 'Credit Sales',\n";
	    s +=   "				fill: false,\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				data: ["+catitemcreditdata+"\n";
	    s +=   "				],\n";
	    s +=   "			}]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Product Category Wise Sales of "+year+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    //s +=   "        				return String.fromCharCode(0x09F3)+' '+ addCommas(tooltipItem.yLabel)+' M';\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Million'\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    s +=   "var catcussalesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			labels: ["+catcusdata+"],\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Cash Sales',\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				borderWidth: 1,\n";
	    s +=   "				data: ["+catcusamtdata+"\n";
	    s +=   "				]\n";
	    //s +=   "				fill: false,\n";
	    /*
	    s +=   "			}, {\n";
	    s +=   "				label: 'Credit Sales',\n";
	    //s +=   "				fill: false,\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				data: ["+itemcreditdata+"\n";
	    s +=   "				]\n";
	    */
	    s +=   "			}]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			elements: {\n";
	    s +=   "				rectangle: {\n";
	    s +=   "					borderWidth: 2,\n";
	    s +=   "				}\n";
	    s +=   "			},\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Product Category Wise Sales of "+year+"'\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
  		s +=   "					},\n";
	    s +=   "				gridLines: {\n";
	    s +=   "					display: false,\n";
	    s +=   "				},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					fontSize:8\n";
	    //s +=   "    					callback: function(value) {\n";
	    //s +=   "        					return addCommas(value)+' M';\n";
	    //s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    //s +=   "			legend: {\n";
	    //s +=   "				position: 'right',\n";
	    //s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";
	    

	    s +=   "var territoryconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			labels: ["+territorydata+"],\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Sales',\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				borderWidth: 1,\n";
	    s +=   "				data: ["+territorysalesdata+"\n";
	    s +=   "				]\n";
	    //s +=   "				fill: false,\n";
	    /*
	    s +=   "			}, {\n";
	    s +=   "				label: 'Credit Sales',\n";
	    //s +=   "				fill: false,\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				data: ["+itemcreditdata+"\n";
	    s +=   "				]\n";
	    */
	    s +=   "			}]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			elements: {\n";
	    s +=   "				rectangle: {\n";
	    s +=   "					borderWidth: 2,\n";
	    s +=   "				}\n";
	    s +=   "			},\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Territory Wise Sales of "+year+"'\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "					}\n";
//	    s +=   "					gridLines: {\n";
//	    s +=   "						display: false,\n";
//	    s +=   "					},\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
  		s +=   "					},\n";
	    s +=   "					gridLines: {\n";
	    s +=   "						display: true,\n";
	    s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					fontSize:8\n";
	    //s +=   "    					callback: function(value) {\n";
	    //s +=   "        					return addCommas(value)+' M';\n";
	    //s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    //s +=   "			legend: {\n";
	    //s +=   "				position: 'right',\n";
	    //s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";
	    

	    s +=   "var spsalesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			labels: ["+spdata+"],\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Sales',\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				borderWidth: 1,\n";
	    s +=   "				data: ["+spsalesdata+"\n";
	    s +=   "				]\n";
	    //s +=   "				fill: false,\n";
	    /*
	    s +=   "			}, {\n";
	    s +=   "				label: 'Credit Sales',\n";
	    //s +=   "				fill: false,\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				data: ["+itemcreditdata+"\n";
	    s +=   "				]\n";
	    */
	    s +=   "			}]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			elements: {\n";
	    s +=   "				rectangle: {\n";
	    s +=   "					borderWidth: 2,\n";
	    s +=   "				}\n";
	    s +=   "			},\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Sales Person Wise Sales of "+year+"'\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					fontSize:10\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
  		s +=   "					},\n";
//	    s +=   "					gridLines: {\n";
//	    s +=   "						display: true,\n";
//	    s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					fontSize:10\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    //s +=   "			legend: {\n";
	    //s +=   "				position: 'right',\n";
	    //s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";
	    
	    s +=   "	window.onload = function() {\n";
	    s +=   "		var ctx = document.getElementById('msales').getContext('2d');\n";
	    s +=   "		var chart = new Chart(ctx, config);\n";
	    s +=   "		var ctx1 = document.getElementById('catitemsales').getContext('2d');\n";
	    s +=   "		var chart1 = new Chart(ctx1, config1);\n";
	    s +=   "		var ctx2 = document.getElementById('catcussales').getContext('2d');\n";
	    s +=   "		var chart2 = new Chart(ctx2, catcussalesconfig);\n";
	    s +=   "		var ctx3 = document.getElementById('territorysales').getContext('2d');\n";
	    s +=   "		var chart3 = new Chart(ctx3, territoryconfig);\n";
	    s +=   "		var ctx4 = document.getElementById('spsales').getContext('2d');\n";
	    s +=   "		var chart4 = new Chart(ctx4, spsalesconfig);\n";
	    //s +=   "		var ctx1 = document.getElementById('catitemsales').getContext('2d');\n";
	    //s +=   "		new Chart(ctx1, config1);\n";
	    //s +=   "window.myLine1.catitemsales.style.height = '208px';\n";
	    //s +=   "window.myLine1.catitemsales.parentNode.style.width = '400px';\n";
	    s +=   "	};\n";
//	    s +=   "var colorNames = Object.keys(window.chartColors);\n";
/*
	    s +=   "	document.getElementById('randomizeData').addEventListener('click', function() {\n";
	    s +=   "		config.data.datasets.forEach(function(dataset) {\n";
	    s +=   "			dataset.data = dataset.data.map(function() {\n";
	    s +=   "				return randomScalingFactor();\n";
	    s +=   "			});\n";

	    s +=   "		});\n";

	    s +=   "		window.myLine.update();\n";
	    s +=   "	});\n";
*/	    
	    
	    s +=   "							</script>\n";
	    //s +=   "						</div>\n";

	  return s;
  }

  public static String general(zabInfo zabinfo){
	  String s = "";
	  String caption = "General";
	  String name = "general";
	  int per = 3;
	  int year = 2020;
	  String id = ""+zabinfo.ses.getAttribute("id");
	  String email = ""+zabinfo.ses.getAttribute("email");
   	  
   	  String deptwisecount = "";
   	  deptwisecount += "	<table id=\"deptwisecount-table\" valign=top class=\"table-bordered\" style=\"width:100%;\"cellpadding=5>\n";
   	  deptwisecount += "		<thead>\n";
   	  deptwisecount += "			<tr>\n"; //class=\"list-header\"
   	  deptwisecount += "				<td style=\"background-color:#ff6384;font-weight:bold\" colspan=4>Department Wise OPD/IPD Count</td>\n";
   	  deptwisecount += "			</tr>\n";
   	  deptwisecount += "			<th>Department Name</th><td style=\"font-weight:bold\" align=\"right\">OPD</td><td style=\"font-weight:bold\" align=\"right\">IPD</td><td align=\"right\" style=\"font-weight:bold\">Canceled Appointment</td>\n";
   	  deptwisecount += "		</thead>\n";
   	  deptwisecount += "		<tbody>\n";
      
	  deptwisecount += "<tr><td>CARDIAC SURGERY</td><td align=\"right\">40</td><td align=\"right\">20</td><td align=\"right\">6</td></tr>\n";
	  deptwisecount += "<tr><td>CARDIOLOGY</td><td align=\"right\">60</td><td align=\"right\">50</td><td align=\"right\">1</td></tr>\n";
	  deptwisecount += "<tr><td>ONCOLOGY</td><td align=\"right\">15</td><td align=\"right\">20</td><td align=\"right\">3</td></tr>\n";
	  deptwisecount += "<tr><td>GASTROENTEROLOGY</td><td align=\"right\">15</td><td align=\"right\">10</td><td align=\"right\">2</td></tr>\n";
	  deptwisecount += "<tr><td>GENERAL SURGERY</td><td align=\"right\">12</td><td align=\"right\">6</td><td align=\"right\">1</td></tr\n";
	  deptwisecount += "<tr><td>INTERNAL MEDICINE</td><td align=\"right\">15</td><td align=\"right\">5</td><td align=\"right\">2</td></tr>\n";
	  deptwisecount += "<tr><td>NEONATOLOGY</td><td align=\"right\">20</td><td align=\"right\">15</td><td align=\"right\">3</td></tr>\n";
	  deptwisecount += "<tr><td>NEPHROLOGY</td><td align=\"right\">30</td><td align=\"right\">20</td><td align=\"right\">5</td></tr>\n";
	  deptwisecount += "<tr><td>NEURO SURGERY</td><td align=\"right\">14</td><td align=\"right\">12</td><td align=\"right\">2</td></tr>\n";
	  deptwisecount += "<tr><td>OBSTETRICS AND GYNAECOLOGY</td><td align=\"right\">35</td><td align=\"right\">25</td><td align=\"right\">6</td></tr>\n";
   	  deptwisecount += "</tbody>";
   	  deptwisecount += "</table>";
   	  
   	  String deptwiseipdinfo = "";
   	  deptwiseipdinfo += "	<table id=\"deptwiseipdinfo-table\" valign=top class=\"table-bordered\" style=\"width:100%;\"cellpadding=5>\n";
   	  deptwiseipdinfo += "		<thead>\n";
   	  deptwiseipdinfo += "			<tr>\n"; 
   	  deptwiseipdinfo += "				<td style=\"background-color:#ff6384;font-weight:bold\" colspan=4>Department Wise IPD Info</td>\n";
   	  deptwiseipdinfo += "			</tr>\n";
   	  deptwiseipdinfo += "			<th>Department Name</th><td style=\"font-weight:bold\" align=\"right\">Admission</td><td style=\"font-weight:bold\" align=\"right\">Discharge</td>\n";
   	  deptwiseipdinfo += "		</thead>\n";
   	  deptwiseipdinfo += "		<tbody>\n";
      
	  deptwiseipdinfo += "<tr><td>CARDIAC SURGERY</td><td align=\"right\">15</td><td align=\"right\">6</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>CARDIOLOGY</td><td align=\"right\">20</td><td align=\"right\">10</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>ONCOLOGY</td><td align=\"right\">25</td><td align=\"right\">12</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>GASTROENTEROLOGY</td><td align=\"right\">15</td><td align=\"right\">10</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>GENERAL SURGERY</td><td align=\"right\">12</td><td align=\"right\">6</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>INTERNAL MEDICINE</td><td align=\"right\">15</td><td align=\"right\">5</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>NEONATOLOGY</td><td align=\"right\">20</td><td align=\"right\">15</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>NEPHROLOGY</td><td align=\"right\">30</td><td align=\"right\">1</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>NEURO SURGERY</td><td align=\"right\">14</td><td align=\"right\">9</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>OBSTETRICS AND GYNAECOLOGY</td><td align=\"right\">30</td><td align=\"right\">5</td></tr>\n";
   	  deptwiseipdinfo += "</tbody>";
   	  deptwiseipdinfo += "</table>";
   	  
   	  String docwiseipdinfo = "";
   	  docwiseipdinfo += "	<table id=\"docwiseipdinfo-table\" valign=top class=\"table-bordered\" style=\"width:100%;\"cellpadding=5>\n";
   	  docwiseipdinfo += "		<thead>\n";
   	  docwiseipdinfo += "			<tr>\n"; //class=\"list-header\"
   	  docwiseipdinfo += "				<td style=\"background-color:#ff6384;font-weight:bold\" colspan=5>Doctor Wise OPD/IPD info</td>\n";
   	  docwiseipdinfo += "			</tr>\n";
   	  docwiseipdinfo += "			<th>Doctor's Name</th><th>Department Name</th><td style=\"font-weight:bold\" align=\"right\">OPD</td><td style=\"font-weight:bold\" align=\"right\">IPD</td><td align=\"right\" style=\"font-weight:bold\">Canceled Appointment</td>\n";
   	  docwiseipdinfo += "		</thead>\n";
   	  docwiseipdinfo += "		<tbody>\n";
      
	  docwiseipdinfo += "<tr><td>Dr. Jahangir   Kabir</td><td>CARDIAC SURGERY</td><td align=\"right\">40</td><td align=\"right\">20</td><td align=\"right\">3</td></tr>\n";
	  docwiseipdinfo += "<tr><td>Dr. N. A. M.   Momenuzzaman</td><td>CARDIOLOGY</td><td align=\"right\">45</td><td align=\"right\">30</td><td align=\"right\">7</td></tr>\n";
	  docwiseipdinfo += "<tr><td>Dr. Kaisar  Nasrullah Khan</td><td>CARDIOLOGY</td><td align=\"right\">15</td><td align=\"right\">20</td><td align=\"right\">5</td></tr>\n";
	  docwiseipdinfo += "<tr><td>Dr. Nargis  Ara Begum</td><td>NEONATOLOGY</td><td align=\"right\">20</td><td align=\"right\">15</td><td align=\"right\">3</td></tr>\n";
	  docwiseipdinfo += "<tr><td>Prof. Nurul   Islam</td><td>NEPHROLOGY</td><td align=\"right\">30</td><td align=\"right\">20</td><td align=\"right\">5</td></tr>\n";
	  docwiseipdinfo += "<tr><td>Dr. Syed  Sayed Ahmed</td><td>NEURO SURGERY</td><td align=\"right\">14</td><td align=\"right\">12</td><td align=\"right\">2</td></tr>\n";
	  docwiseipdinfo += "<tr><td>Dr. Naseem   Mahmud</td><td>OBSTETRICS AND GYNAECOLOGY</td><td align=\"right\">35</td><td align=\"right\">25</td><td align=\"right\">0</td></tr>\n";
   	  docwiseipdinfo += "</tbody>";
   	  docwiseipdinfo += "</table>";
   	  
   	  
   	  
   	  	s  =   "<table id=\"table-cap-filter-graph\" align=center valign=top border=0 cellpadding=10 cellspacing=10 width=99% height=100%>\n";
   	  	s +=   "	<tr>\n";
	  	s +=   "		<td align=left valign=top width=70%>\n";
	  	s +=   "			<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; // 2nd  tr with graph
	    //s +=   "					<td>\n";
	    //s +=   "						<table id=\"table-hs-3rd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% >\n";
	    //s +=   "							<tr>\n"; 
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"mwopd\" height=160px></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"mwipd\" height=\"160px\"></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    s +=   "				<tr>\n"; // 3rd  tr with graph
	    s +=   "					<td colspan=4 class=\"hs-info-td\">\n";
	    
	    //********************** 3rd row ***************************************//
	    s +=   "						<table id=\"table-hs-2nd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% height=100%>\n";
	    s +=   "							<tr>\n"; 
	    s +=   "								<td width=34% valign=top>\n";
	    s +=   "									<div style=\"font-size:13px;margin-right:10px;background-color:#fff;\">\n";
	    s +=   docwiseipdinfo;
	    s +=   "									</div>\n";
	    s +=   "	 							</td>\n";
	    s +=   "								<td width=33% valign=top>\n";
	    s +=   "									<div style=\"font-size:13px;margin-right:10px;background-color:#fff;\">\n";
	    s +=   deptwisecount;
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "								<td width=32% valign=top>\n";
	    s +=   "									<div style=\"font-size:13px;margin-right:10px;background-color:#fff;\">\n";
	    s +=   deptwiseipdinfo;
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow
	    
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    //s +=   "			</td>\n";  // end of 80% td
	    //s +=   "		</tr>\n"; 
	    //s +=   "	</table>\n"; 
	    s +=   "</td>\n"; // end of 80% td
	    s +=   "	</tr>\n";
	    s +=   "</table>\n";
	    
	    s +=   "			<script>\n";
	    s +=   "var config = {\n";
	    s +=   "		type: 'bar',\n";	
		s +=   "		data: {\n";
	    s +=   "			labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'OPD',\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				data: [2300,2500,1800,3000,1650,2800,1560,0,0,0,0,0],\n";
	    s +=   "				fill: false,\n";
	    s +=   "			}]\n";
	    s +=   "		},\n";
		
		
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Month Wise OPD Info'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Patient Count'\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";
	    
	    s +=   "var ipdconfig = {\n";
	    s +=   "		type: 'line',\n";
	    s +=   "		data: {\n";
	    s +=   "			labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Admission',\n";
	    s +=   "				backgroundColor: window.chartColors.orange,\n";
	    s +=   "				borderColor: window.chartColors.orange,\n";
	    s +=   "				data: [2500,2800,2300,2700,2500,2300,1800,0,0,0,0,0],\n";
	    s +=   "				fill: false,\n";
	    s +=   "			}, {\n";
	    s +=   "				label: 'Emergency',\n";
	    s +=   "				fill: false,\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				data: [700,500,900,550,950,778,530,0,0,0,0,0],\n";
		s +=   "			}, {\n";
	    s +=   "				label: 'IPD',\n";
	    s +=   "				fill: false,\n";
	    s +=   "				backgroundColor: window.chartColors.purple,\n";
	    s +=   "				borderColor: window.chartColors.purple,\n";
	    s +=   "				data: [1800,2300,1400,2150,1550,1522,1270,0,0,0,0,0],\n";
	    s +=   "			}]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Month Wise Admission/ Emergency/ IPD Info'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Patient Count'\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";
	    s +=   "	window.onload = function() {\n";
	    s +=   "		var ctx = document.getElementById('mwopd').getContext('2d');\n";
	    s +=   "		var chart = new Chart(ctx, config);\n";
	    s +=   "		var ctx1 = document.getElementById('mwipd').getContext('2d');\n";
	    s +=   "		var chart1 = new Chart(ctx1, ipdconfig);\n";
	    /*
	    s +=   "		var ctx2 = document.getElementById('catcussales').getContext('2d');\n";
	    s +=   "		var chart2 = new Chart(ctx2, catcussalesconfig);\n";
	    s +=   "		var ctx3 = document.getElementById('territorysales').getContext('2d');\n";
	    s +=   "		var chart3 = new Chart(ctx3, territoryconfig);\n";
	    s +=   "		var ctx4 = document.getElementById('spsales').getContext('2d');\n";
	    s +=   "		var chart4 = new Chart(ctx4, spsalesconfig);\n";
	    */
	    s +=   "	};\n";
//	    s +=   "var colorNames = Object.keys(window.chartColors);\n";
/*
	    s +=   "	document.getElementById('randomizeData').addEventListener('click', function() {\n";
	    s +=   "		config.data.datasets.forEach(function(dataset) {\n";
	    s +=   "			dataset.data = dataset.data.map(function() {\n";
	    s +=   "				return randomScalingFactor();\n";
	    s +=   "			});\n";

	    s +=   "		});\n";

	    s +=   "		window.myLine.update();\n";
	    s +=   "	});\n";
*/	    
	    
	    s +=   "							</script>\n";
	    //s +=   "						</div>\n";

	  return s;
  }

  public static String doctor(zabInfo zabinfo){
	  String s = "";
	  String caption = "General";
	  String name = "general";
	  int per = 3;
	  int year = 2020;
	  String id = ""+zabinfo.ses.getAttribute("id");
	  String email = ""+zabinfo.ses.getAttribute("email");
	  String docname = ""+zabinfo.ses.getAttribute("empname");
	  String desig = ""+zabinfo.ses.getAttribute("empdesig");
	  String deptname = ""+zabinfo.ses.getAttribute("empdeptname");
   	  
   	  String otsch = "";
   	  otsch += "<table id=\"otsch-table\" valign=top class=\"table-bordered\" style=\"border-radius:5px;width:100%;\"cellpadding=5>\n";
   	  otsch += "	<thead>\n";
   	  otsch += "		<tr>\n"; //class=\"list-header\"
   	  otsch += "			<td style=\"padding:10px;background-color:#ff6384;font-weight:bold\" colspan=6>OT Schedule</td>\n";
   	  otsch += "		</tr>\n";
   	  otsch += "			<th>Slot No</th><th>OT Time</th><th>Patient ID</th><th>Patient Name</th><th>Name of Procedure</th><th>Chief Complain</th>\n";
   	  otsch += "		</thead>\n";
   	  otsch += "		<tbody>\n";
      
	  otsch += "			<tr><td>1</td><td>13:00:00</td><td>1000002879</td><td>SULTANA  PARVIN</td><td>Cryosurgery of Cervix</td><td></td></tr>\n";
	  otsch += "			<tr><td>2</td><td>16:30:00</td><td>1000000468</td><td>ASMA CHOUDHURY</td><td>Laparotomy for Ectopic Pregnancy</td><td></td></tr>\n";
	  otsch += "			<tr><td>3</td><td>18:30:00</td><td>1000000467</td><td>NAZMA  SHAFI</td><td>Caeserean Section</td><td></td></tr>\n";
	  otsch += "			<tr><td>4</td><td>20:30:00</td><td>1000000478</td><td>SHARMIN  SULTANA</td><td>Caeserean Section</td><td></td></tr>\n";
   	  otsch += "		</tbody>";
   	  otsch += "</table>";
   	  
   	  String deptwiseipdinfo = "";
   	  deptwiseipdinfo += "	<table id=\"deptwiseipdinfo-table\" valign=top class=\"table-bordered\" style=\"width:100%;\"cellpadding=5>\n";
   	  deptwiseipdinfo += "		<thead>\n";
   	  deptwiseipdinfo += "			<tr>\n"; 
   	  deptwiseipdinfo += "				<td style=\"background-color:#ff6384;font-weight:bold\" colspan=4>Department Wise IPD Info</td>\n";
   	  deptwiseipdinfo += "			</tr>\n";
   	  deptwiseipdinfo += "			<th>Department Name</th><td style=\"font-weight:bold\" align=\"right\">Admission</td><td style=\"font-weight:bold\" align=\"right\">Discharge</td>\n";
   	  deptwiseipdinfo += "		</thead>\n";
   	  deptwiseipdinfo += "		<tbody>\n";
      
	  deptwiseipdinfo += "<tr><td>CARDIAC SURGERY</td><td align=\"right\">15</td><td align=\"right\">6</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>CARDIOLOGY</td><td align=\"right\">20</td><td align=\"right\">10</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>ONCOLOGY</td><td align=\"right\">25</td><td align=\"right\">12</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>GASTROENTEROLOGY</td><td align=\"right\">15</td><td align=\"right\">10</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>GENERAL SURGERY</td><td align=\"right\">12</td><td align=\"right\">6</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>INTERNAL MEDICINE</td><td align=\"right\">15</td><td align=\"right\">5</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>NEONATOLOGY</td><td align=\"right\">20</td><td align=\"right\">15</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>NEPHROLOGY</td><td align=\"right\">30</td><td align=\"right\">1</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>NEURO SURGERY</td><td align=\"right\">14</td><td align=\"right\">9</td></tr>\n";
	  deptwiseipdinfo += "<tr><td>OBSTETRICS AND GYNAECOLOGY</td><td align=\"right\">30</td><td align=\"right\">5</td></tr>\n";
   	  deptwiseipdinfo += "</tbody>";
   	  deptwiseipdinfo += "</table>";
   	  
   	  String applist = "";
   	  applist += "<table id=\"applist-table\" valign=top class=\"table-bordered\" style=\"border-radius:5px;width:100%;\"cellpadding=5>\n";
   	  applist += "	<thead>\n";
   	  applist += "		<tr>\n"; //class=\"list-header\"
   	  applist += "			<td style=\"padding:10px;background-color:#ff6384;font-weight:bold\" colspan=6>Today's List of Appointments</td>\n";
   	  applist += "		</tr>\n";
   	  applist += "			<th>Slot No</th><th>Appointment Time</th><th>Patient ID</th><th>Patient Name</th><th>Status</th><th>Note</th>\n";
   	  applist += "		</thead>\n";
   	  applist += "		<tbody>\n";
      
	  applist += "			<tr><td>1</td><td>9:00:00</td><td>1000000002</td><td>ANOWARA RAHMAN</td><td>Confirmed</td><td></td></tr>\n";
	  applist += "			<tr><td>2</td><td>9:30:00</td><td>1000000540</td><td>ABDUL MANNAN KHAN</td><td>Confirmed</td><td></td></tr>\n";
	  applist += "			<tr><td>3</td><td>10:00:00</td><td>1000000004</td><td>M. A. RAHIM</td><td>Confirmed</td><td></td></tr>\n";
	  applist += "			<tr><td>4</td><td>10:30:00</td><td>1000000007</td><td>MALIK MAJEED</td><td>Booked</td><td></td></tr>\n";
	  applist += "			<tr><td>5</td><td>11:00:00</td><td>1000000118</td><td>MAHMUDA AKHTER</td><td>Booked</td><td></td></tr>\n";
	  applist += "			<tr><td>8</td><td>12:30:00</td><td>1000000112</td><td>ZAINUL ABEDIN</td><td>Confirmed</td><td></td></tr>\n";
	  applist += "			<tr><td>14</td><td>15:30:00</td><td>1000000431</td><td>ABUL HASEM KHAN</td><td>Booked</td><td></td></tr>\n";
   	  applist += "		</tbody>";
   	  applist += "</table>";
   	  
   	  
   	  
   	  	s  =   "<table id=\"table-cap-filter-graph\" align=center valign=top border=0 cellpadding=10 cellspacing=10 width=99% height=100%>\n";
	    s +=   "	<tr>\n";
	    s +=   "		<td colspan=2 align=left>\n";
	    s +=   "			<div style=\"padding:10px;height:50px;background-color:#fff;box-shadow:1px 1px 2px rgba(0, 0, 0, 0.4);font-size:20px\">"+docname+", "+desig+", "+deptname+"</div>\n";
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n";
   	  	s +=   "	<tr>\n";
	  	s +=   "		<td align=left valign=top width=70%>\n";
	  	
	  	//************************ 1st Row ************************************//
	  	
	    
	    s +=   "						<table id=\"table-hs-2nd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% height=100%>\n";
	    s +=   "							<tr>\n"; 
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"font-size:13px;margin-right:10px;background-color:#fff;\">\n";
	    s +=   applist;
	    s +=   "									</div>\n";
	    s +=   "	 							</td>\n";
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"font-size:13px;background-color:#fff;\">\n";
	    s +=   otsch;
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    /*
	    s +=   "								<td width=32% valign=top>\n";
	    s +=   "									<div style=\"font-size:13px;margin-right:10px;background-color:#fff;\">\n";
	    s +=   deptwiseipdinfo;
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    */
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow


	    //************************ end 1st Row ************************************//

	    
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    s +=   "				<tr>\n"; // 3rd  tr with graph
	    s +=   "					<td colspan=4 class=\"hs-info-td\">\n";
	    
	    //********************** 2nd row ***************************************//

	  	s +=   "			<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; // 2nd  tr with graph
	    //s +=   "					<td>\n";
	    //s +=   "						<table id=\"table-hs-3rd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% >\n";
	    //s +=   "							<tr>\n"; 
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"mwopd\" height=160px></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"mwipd\" height=\"160px\"></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow
	    
	    //********************** end 2nd row ***************************************//
	    
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    //s +=   "			</td>\n";  // end of 80% td
	    //s +=   "		</tr>\n"; 
	    //s +=   "	</table>\n"; 
	    s +=   "</td>\n"; // end of 80% td
	    s +=   "	</tr>\n";
	    s +=   "</table>\n";
	    
	    s +=   "			<script>\n";
	    s +=   "var config = {\n";
	    s +=   "		type: 'line',\n";
	    s +=   "		data: {\n";
	    s +=   "			labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'OPD',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				data: [300,200,270,260,250,310,300,0,0,0,0,0],\n";
	    s +=   "				fill: false,\n";
	    s +=   "			}, {\n";
	    s +=   "				label: 'IPD',\n";
	    s +=   "				fill: false,\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				data: [120,130,160,200,50,110,60,0,0,0,0,0],\n";
	    s +=   "			}]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Month Wise OPD/IPD Info'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Patient Count'\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";
	    
	    s +=   "var ipdconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Procedure Done',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				data: [151,106,176,143,152,131,134,0,0,0,0,0],\n";
	    s +=   "				fill: false,\n";
	    s +=   "			}]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Month Wise Procedure Info'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Patient Count'\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";
	    s +=   "	window.onload = function() {\n";
	    s +=   "		var ctx = document.getElementById('mwopd').getContext('2d');\n";
	    s +=   "		var chart = new Chart(ctx, config);\n";
	    s +=   "		var ctx1 = document.getElementById('mwipd').getContext('2d');\n";
	    s +=   "		var chart1 = new Chart(ctx1, ipdconfig);\n";
	    /*
	    s +=   "		var ctx2 = document.getElementById('catcussales').getContext('2d');\n";
	    s +=   "		var chart2 = new Chart(ctx2, catcussalesconfig);\n";
	    s +=   "		var ctx3 = document.getElementById('territorysales').getContext('2d');\n";
	    s +=   "		var chart3 = new Chart(ctx3, territoryconfig);\n";
	    s +=   "		var ctx4 = document.getElementById('spsales').getContext('2d');\n";
	    s +=   "		var chart4 = new Chart(ctx4, spsalesconfig);\n";
	    */
	    s +=   "	};\n";
//	    s +=   "var colorNames = Object.keys(window.chartColors);\n";
/*
	    s +=   "	document.getElementById('randomizeData').addEventListener('click', function() {\n";
	    s +=   "		config.data.datasets.forEach(function(dataset) {\n";
	    s +=   "			dataset.data = dataset.data.map(function() {\n";
	    s +=   "				return randomScalingFactor();\n";
	    s +=   "			});\n";

	    s +=   "		});\n";

	    s +=   "		window.myLine.update();\n";
	    s +=   "	});\n";
*/	    
	    
	    s +=   "							</script>\n";
	    //s +=   "						</div>\n";

	  return s;
  }

  public static String management(zabInfo zabinfo){
	  
	  String s = "";
	  String caption = "General";
	  String name = "general";
	  int per = 3;
	  int year = 2020;
	  String id = ""+zabinfo.ses.getAttribute("id");
	  String email = ""+zabinfo.ses.getAttribute("email");
	  String docname = ""+zabinfo.ses.getAttribute("empname");
	  String desig = ""+zabinfo.ses.getAttribute("empdesig");
	  String deptname = ""+zabinfo.ses.getAttribute("empdeptname");
   	  
   	  String proclist = "";
   	  proclist += "	<table id=\"proclist-table\" valign=top class=\"table-bordered\" style=\"width:100%;\"cellpadding=5>\n";
   	  proclist += "		<thead>\n";
   	  proclist += "			<tr>\n"; //class=\"list-header\"
   	  proclist += "				<td style=\"padding:10px;background-color:#ff6384;font-weight:bold\" colspan=5>Doctor Wise Procedure</td>\n";
   	  proclist += "			</tr>\n";
   	  proclist += "			<th>Doctor's Name</th><th>Department Name</th><td style=\"font-weight:bold\" align=\"right\">Procedure Done Today</td><td style=\"font-weight:bold\" align=\"right\">Procedure Done YTD</td>\n";
   	  proclist += "		</thead>\n";
   	  proclist += "		<tbody>\n";
      
	  proclist += "			<tr><td>Dr. Jahangir   Kabir</td><td>Cardiac Surgery</td><td align=\"right\">4</td><td align=\"right\">130</td></tr>\n";
	  proclist += "			<tr><td>Dr. N. A. M.   Momenuzzaman</td><td>Cardiology</td><td align=\"right\">10</td><td align=\"right\">260</td></tr>\n";
	  proclist += "			<tr><td>Dr. Kaisar  Nasrullah Khan</td><td>Cardiology</td><td align=\"right\">6</td><td align=\"right\">200</td></tr>\n";
	  proclist += "			<tr><td>Dr. Fatima Begum</td><td>Cardiology</td><td align=\"right\">2</td><td align=\"right\">50</td></tr>\n";
	  proclist += "			<tr><td>Dr. Syed  Sayed Ahmed</td><td>Neuro Surgery</td><td align=\"right\">2</td><td align=\"right\">20</td></tr>\n";
	  proclist += "			<tr><td>Dr. Anisur Rahman</td><td>General Surgery</td><td align=\"right\">30</td><td align=\"right\">20</td></tr>\n";
	  proclist += "			<tr><td>Dr. Naseem   Mahmud</td><td>Obstetrics & Gynaecology</td><td align=\"right\">35</td><td align=\"right\">25</td></tr>\n";
	  proclist += "			<tr><td></td><td>Total</td><td align=\"right\">89</td><td align=\"right\">705</td></tr>\n";
   	  proclist += "		</tbody>";
   	  proclist += "	</table>";
   	  
   	  String prdlist = "";
   	  prdlist += "	<table id=\"prdlist-table\" valign=top class=\"table-bordered\" style=\"width:100%;\"cellpadding=5>\n";
   	  prdlist += "		<thead>\n";
   	  prdlist += "			<tr>\n"; //class=\"list-header\"
   	  prdlist += "				<td style=\"padding:10px;background-color:#ff6384;font-weight:bold\" colspan=5>Revenue By Cost Center (Million)</td>\n";
   	  prdlist += "			</tr>\n";
   	  prdlist += "			<th>Department Name</th><td style=\"font-weight:bold\" align=\"right\">Revenue This Month</td><td style=\"font-weight:bold\" align=\"right\">Revenue YTD</td>\n";
   	  prdlist += "		</thead>\n";
   	  prdlist += "		<tbody>\n";
      
	  prdlist += "			<tr><td>Cardiac Surgery</td><td align=\"right\">3</td><td align=\"right\">124</td></tr>\n";
	  prdlist += "			<tr><td>Cardiology</td><td align=\"right\">15</td><td align=\"right\">361</td></tr>\n";
	  prdlist += "			<tr><td>Obstetrics & Gynaecology</td><td align=\"right\">6</td><td align=\"right\">200</td></tr>\n";
	  prdlist += "			<tr><td>Nephrology</td><td align=\"right\">2.6</td><td align=\"right\">20</td></tr>\n";
	  prdlist += "			<tr><td>Vaccination</td><td align=\"right\">2</td><td align=\"right\">50</td></tr>\n";
	  prdlist += "			<tr><td>Neuro Surgery</td><td align=\"right\">2</td><td align=\"right\">20</td></tr>\n";
	  prdlist += "			<tr><td>Food & Beverage</td><td align=\"right\">30</td><td align=\"right\">20</td></tr>\n";
	  prdlist += "			<tr><td>Blood Bank</td><td align=\"right\">35</td><td align=\"right\">25</td></tr>\n";
	  prdlist += "			<tr><td>Total</td><td align=\"right\">89</td><td align=\"right\">705</td></tr>\n";
   	  prdlist += "		</tbody>";
   	  prdlist += "	</table>";
   	  
   	  
   	  	s  =   "<table id=\"table-cap-filter-graph\" align=center valign=top border=0 cellpadding=10 cellspacing=10 width=99% height=100%>\n";
/*
   	  	s +=   "	<tr>\n";
	    s +=   "		<td colspan=2 align=left>\n";
	    s +=   "			<div style=\"padding:10px;height:50px;background-color:#fff;box-shadow:1px 1px 2px rgba(0, 0, 0, 0.4);font-size:20px\">"+docname+", "+desig+", "+deptname+"</div>\n";
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n";
*/	    
	    s +=   "				<tr>\n";

	    /*********************** OPD *************************************/
	    
	    s +=   "					<td class=\"hs-info-td\">\n";
	    s +=   "						<div class=\"card hs-card-sm\">\n";
	    s +=   "							<div class=\"card-header\" style=\"height:50px;\" >\n";
	    s +=   "								<img class=\"hs-info-img\" src=\""+zabConf.imagePath+"\\icons\\sales.png\"/><div class=\"hs-info-title\">OPD</div>\n"; 
	    s +=   "							</div>\n";
	    s +=   "							<div class=\"card-body hs-info-content\">\n";
	    s +=   "								<div><span style=\"font-size:16px;fcolor:#3f48cc;\">৳ Revenue   : 1.20 (M)</span></div>\n";
	    s +=   "								<div><span style=\"font-size:16px;color:#f53794;\">৳ Collection: 1.18 (M)</span></div>\n";
    	s +=   "							<br><div style=\"font-size:13px;color:#dc3545;\">Over Yesterday 5.8 % <i class=\"fa fa-arrow-up\"></i></div>\n";
	    s +=   "							</div>\n"; // end of card-body
	    s +=   "						</div>\n"; // end of card
	    s +=   "					</td>\n"; // end of first td class="hs-info-td"
	    
	    /*********************** end of OPD *************************************/

	    /*********************** IPD *************************************/
	    
	    s +=   "					<td class=\"hs-info-td\">\n";
	    s +=   "						<div class=\"card hs-card-sm\">\n";
	    s +=   "							<div class=\"card-header\" style=\"height:50px;\" >\n";
	    s +=   "								<img class=\"hs-info-img\" src=\""+zabConf.imagePath+"\\icons\\sales.png\"/><div class=\"hs-info-title\">Admission, A & E</div>\n"; 
	    s +=   "							</div>\n";
	    s +=   "							<div class=\"card-body hs-info-content\">\n";
	    s +=   "								<div><span style=\"font-size:16px;color:#3f48cc;\">৳ Revenue   : 2.20 (M)</span></div>\n";
	    s +=   "								<div><span style=\"font-size:16px;color:#f53794;\">৳ Collection: 2.17 (M)</span></div>\n";
    	s +=   "							<br><div style=\"font-size:13px;color:#dc3545;\">Over Yesterday 2 % <i class=\"fa fa-arrow-down\"></i></div>\n";
	    s +=   "							</div>\n"; // end of card-body
	    s +=   "						</div>\n"; // end of card
	    s +=   "					</td>\n"; // end of first td class="hs-info-td"
	    
	    /*********************** end IPD *************************************/

	    /*********************** Lab/Radiology *************************************/
	    
	    s +=   "					<td class=\"hs-info-td\">\n";
	    s +=   "						<div class=\"card hs-card-sm\">\n";
	    s +=   "							<div class=\"card-header\" style=\"height:50px;\" >\n";
	    s +=   "								<img class=\"hs-info-img\" src=\""+zabConf.imagePath+"\\icons\\sales.png\"/><div class=\"hs-info-title\">Lab & Radiology</div>\n"; 
	    s +=   "							</div>\n";
	    s +=   "							<div class=\"card-body hs-info-content\">\n";
	    s +=   "								<div><span style=\"font-size:16px;color:#3f48cc;\">৳ Revenue   : 0.92 (M)</span></div>\n";
	    s +=   "								<div><span style=\"font-size:16px;color:#f53794;\">৳ Collection: 0.89 (M)</span></div>\n";
    	s +=   "								<br><div style=\"font-size:13px;color:#dc3545;\">Over Yesterday 0.20 % <i class=\"fa fa-arrow-up\"></i></div>\n";
	    s +=   "							</div>\n"; // end of card-body
	    s +=   "						</div>\n"; // end of card
	    s +=   "					</td>\n"; // end of first td class="hs-info-td"
	    
	    /*********************** end Lab/Radiology *************************************/
	    
	    /*********************** Pharmacy *************************************/
	    
	    s +=   "					<td class=\"hs-info-td\">\n";
	    s +=   "						<div class=\"card hs-card-sm\">\n";
	    s +=   "							<div class=\"card-header\" style=\"height:50px;\" >\n";
	    s +=   "								<img class=\"hs-info-img\" src=\""+zabConf.imagePath+"\\icons\\sales.png\"/><div class=\"hs-info-title\">Onchology</div>\n"; 
	    s +=   "							</div>\n";
	    s +=   "							<div class=\"card-body hs-info-content\">\n";
	    s +=   "								<div><span style=\"font-size:16px;color:#3f48cc;\">৳ Revenue   : 0.32 (M)</span></div>\n";
	    s +=   "								<div><span style=\"font-size:16px;color:#f53794;\">৳ Collection: 0.31 (M)</span></div>\n";
    	s +=   "								<br><div style=\"font-size:13px;color:#dc3545;\">Over Yesterday 0.20 % <i class=\"fa fa-arrow-up\"></i></div>\n";
	    s +=   "							</div>\n"; // end of card-body
	    s +=   "						</div>\n"; // end of card
	    s +=   "					</td>\n"; // end of first td class="hs-info-td"
	    
	    /*********************** end Lab/Radiology *************************************/
	    
	    /*********************** Total *************************************/
	    
	    s +=   "					<td class=\"hs-info-td\" >\n";
	    s +=   "						<div class=\"card hs-card-sm\">\n";
	    s +=   "							<div class=\"card-header\" style=\"height:50px;\" >\n";
	    s +=   "								<img class=\"hs-info-img\" src=\""+zabConf.imagePath+"\\icons\\sales.png\"/><div class=\"hs-info-title\">Total</div>\n"; 
	    s +=   "							</div>\n";
	    s +=   "							<div class=\"card-body hs-info-content\">\n";
	    s +=   "								<div><span style=\"font-size:16px;color:#3f48cc;\">৳ Revenue   :  4.64 (M)</span></div>\n";
	    s +=   "								<div><span style=\"font-size:16px;color:#f53794;\">৳ Collection:  4.55 (M)</span></div>\n";
    	s +=   "								<br><div style=\"font-size:13px;color:#dc3545;\">Over Yesterday 4.44 % <i class=\"fa fa-arrow-up\"></i></div>\n";
	    s +=   "							</div>\n"; // end of card-body
	    s +=   "						</div>\n"; // end of card
	    s +=   "					</td>\n"; // end of first td class="hs-info-td"
	   
	    /*********************** end Total *************************************/

	    /*********************** Total Patient*************************************/
	    
	    s +=   "					<td class=\"hs-info-td\" >\n";
	    s +=   "						<div class=\"card hs-card-sm\">\n";
	    s +=   "							<div class=\"card-header\" style=\"height:50px;\" >\n";
	    s +=   "								<img class=\"hs-info-img\" src=\""+zabConf.imagePath+"\\icons\\sales.png\"/><div class=\"hs-info-title\">Total Patient</div>\n"; 
	    s +=   "							</div>\n";
	    s +=   "							<div class=\"card-body hs-info-content\">\n";
	    s +=   "								<div><span style=\"font-size:16px;color:#3f48cc;\">OPD :  450</span></div>\n";
	    s +=   "								<div><span style=\"font-size:16px;color:#f53794;\">IPD :  280</span></div>\n";
	    s +=   "								<div><span style=\"font-size:16px;\">Emg.:  120</span></div>\n";
    	s +=   "								<div style=\"font-size:13px;color:#dc3545;\">Over Yesterday 2.32 % <i class=\"fa fa-arrow-up\"></i></div>\n";
	    s +=   "							</div>\n"; // end of card-body
	    s +=   "						</div>\n"; // end of card
	    s +=   "					</td>\n"; // end of first td class="hs-info-td"
	   
	    /*********************** end Total Patient*************************************/

	    s +=   "				</tr>\n"; // end of first tr

	    /*********************** 2nd Row *************************************/

   	  	s +=   "	<tr>\n";
	  	s +=   "		<td align=left valign=top colspan=6>\n";
	    s +=   "			<table id=\"table-hs-2nd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% height=100%>\n";
	    s +=   "				<tr>\n"; 
	    s +=   "					<td width=50% valign=top>\n";
	    s +=   "						<div style=\"font-size:13px;margin-right:10px;background-color:#fff;\">\n";
	    s +=   proclist;
	    s +=   "						</div>\n";
	    s +=   "	 				</td>\n";
	    s +=   "					<td width=50% valign=top>\n";
	    s +=   "						<div style=\"font-size:13px;background-color:#fff;\">\n";
	    s +=   prdlist;
	    s +=   "						</div>\n";
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "			</table>\n"; // end table-hs-2ndrow
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n"; // end 2nd  tr with graph

	    //************************ end 1st Row ************************************//

	    s +=   "	<tr>\n"; // 3rd  tr with graph
	    s +=   "					<td colspan=6 class=\"hs-info-td\">\n";
	    
	    //********************** 2nd row ***************************************//

	  	s +=   "			<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; // 2nd  tr with graph
	    //s +=   "					<td>\n";
	    //s +=   "						<table id=\"table-hs-3rd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% >\n";
	    //s +=   "							<tr>\n"; 
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"mwopd\" height=160px></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"mwipd\" height=\"160px\"></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow
	    
	    //********************** end 2nd row ***************************************//
	    
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    //s +=   "			</td>\n";  // end of 80% td
	    //s +=   "		</tr>\n"; 
	    //s +=   "	</table>\n"; 
	    s +=   "</td>\n"; // end of 80% td
	    s +=   "	</tr>\n";
	    s +=   "</table>\n";
	    
	    s +=   "			<script>\n";
	    s +=   "var config = {\n";
	    s +=   "		type: 'line',\n";
	    s +=   "		data: {\n";
	    s +=   "			labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'OPD',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				data: [300,200,270,260,250,310,300,0,0,0,0,0],\n";
	    s +=   "				fill: false,\n";
	    s +=   "			}, {\n";
	    s +=   "				label: 'IPD',\n";
	    s +=   "				fill: false,\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				data: [120,130,160,200,50,110,60,0,0,0,0,0],\n";
	    s +=   "			}]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Month Wise OPD/IPD Info'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Patient Count'\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";
	    
	    s +=   "var ipdconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Procedure Done',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				data: [151,106,176,143,152,131,134,0,0,0,0,0],\n";
	    s +=   "				fill: false,\n";
	    s +=   "			}]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Month Wise Procedure Info'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Patient Count'\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";
	    s +=   "	window.onload = function() {\n";
	    s +=   "		var ctx = document.getElementById('mwopd').getContext('2d');\n";
	    s +=   "		var chart = new Chart(ctx, config);\n";
	    s +=   "		var ctx1 = document.getElementById('mwipd').getContext('2d');\n";
	    s +=   "		var chart1 = new Chart(ctx1, ipdconfig);\n";
	    /*
	    s +=   "		var ctx2 = document.getElementById('catcussales').getContext('2d');\n";
	    s +=   "		var chart2 = new Chart(ctx2, catcussalesconfig);\n";
	    s +=   "		var ctx3 = document.getElementById('territorysales').getContext('2d');\n";
	    s +=   "		var chart3 = new Chart(ctx3, territoryconfig);\n";
	    s +=   "		var ctx4 = document.getElementById('spsales').getContext('2d');\n";
	    s +=   "		var chart4 = new Chart(ctx4, spsalesconfig);\n";
	    */
	    s +=   "	};\n";
	    
	    s +=   "							</script>\n";
	    //s +=   "						</div>\n";

	  return s;
  }
  public static String patient(zabInfo zabinfo){
	  String s = "";
	  String caption = "General";
	  String name = "general";
	  int per = 3;
	  int year = 2020;
	  String id = ""+zabinfo.ses.getAttribute("id");
	  String email = ""+zabinfo.ses.getAttribute("email");
	  String docname = "1000023452";
	  String desig = "Jainul Abedin";
	  String deptname = "Neuro Surgery";
   	  
   	  String otsch = "";
   	  otsch += "<table id=\"otsch-table\" valign=top class=\"table-bordered\" style=\"border-radius:5px;width:100%;\"cellpadding=5>\n";
   	  otsch += "	<thead>\n";
   	  otsch += "		<tr>\n"; //class=\"list-header\"
   	  otsch += "			<td style=\"padding:10px;background-color:#ff6384;font-weight:bold\" colspan=6>OT Info</td>\n";
   	  otsch += "		</tr>\n";
   	  otsch += "			<th>Serial No</th><th>OT Date</th><th>Name of Procedure</th><th>Chief Complain</th>\n";
   	  otsch += "		</thead>\n";
   	  otsch += "		<tbody>\n";
      
	  otsch += "			<tr><td>1</td><td>04-Jan-2013</td><td>Anterior Cervical Discectomy and Fusion With More Than Two Level</td><td></td><td></td></tr>\n";
	  otsch += "			<tr><td>2</td><td>04-Jan-2013</td><td>Anterior Cervical Microdisectomy and Fusion with Cages - Four Lavel</td><td></td></tr>\n";
   	  otsch += "		</tbody>";
   	  otsch += "</table>";
   	  
   	  String testinfo = "";
   	  testinfo += "<table id=\"testinfo-table\" valign=top class=\"table-bordered\" style=\"border-radius:5px;width:100%;\"cellpadding=5>\n";
   	  testinfo += "	<thead>\n";
   	  testinfo += "		<tr>\n"; //class=\"list-header\"
   	  testinfo += "			<td style=\"padding:10px;background-color:#ff6384;font-weight:bold\" colspan=6>Lab Test Info</td>\n";
   	  testinfo += "		</tr>\n";
   	  testinfo += "			<th>Serial No</th><th>Test Date</th><th>Name of Test</th><th>Status</th><th>Ref</th>\n";
   	  testinfo += "		</thead>\n";
   	  testinfo += "		<tbody>\n";
      
	  testinfo += "			<tr><td>1</td><td>04-Jan-2013</td><td><a href=#>CBC</a></td><td>Done</td><td></td></tr>\n";
	  testinfo += "			<tr><td>2</td><td>04-Jan-2013</td><td><a href=#>CBC+ESR</a></td><td>Done</td><td></td></tr>\n";
	  testinfo += "			<tr><td>2</td><td>04-Jan-2013</td><td><a href=#>Urine Culture</a></td><td>Done</td><td></td></tr>\n";
	  testinfo += "			<tr><td>2</td><td>04-Jan-2013</td><td><a href=#>Urine R/E</a></td><td>Done</td><td></td></tr>\n";
   	  testinfo += "		</tbody>";
   	  testinfo += "</table>";
   	  
   	  String rtestinfo = "";
   	  rtestinfo += "<table id=\"rtestinfo-table\" valign=top class=\"table-bordered\" style=\"border-radius:5px;width:100%;\"cellpadding=5>\n";
   	  rtestinfo += "	<thead>\n";
   	  rtestinfo += "		<tr>\n"; //class=\"list-header\"
   	  rtestinfo += "			<td style=\"padding:10px;background-color:#ff6384;font-weight:bold\" colspan=6>Radiology Test Info</td>\n";
   	  rtestinfo += "		</tr>\n";
   	  rtestinfo += "			<th>Serial No</th><th>Test Date</th><th>Name of Test</th><th>Status</th><th>Ref</th>\n";
   	  rtestinfo += "		</thead>\n";
   	  rtestinfo += "		<tbody>\n";
      
	  rtestinfo += "			<tr><td>1</td><td>05-Jan-2013</td><td><a href=#>CT- Arm Left Side</a></td><td>Done</td><td></td></tr>\n";
	  rtestinfo += "			<tr><td>2</td><td>05-Jan-2013</td><td><a href=#>MRI of Hand +  Joints  -  Bilateral</a></td><td>Done</td><td></td></tr>\n";
   	  rtestinfo += "		</tbody>";
   	  rtestinfo += "</table>";
   	  
   	  String applist = "";
   	  applist += "<table id=\"applist-table\" valign=top class=\"table-bordered\" style=\"border-radius:5px;width:100%;\"cellpadding=5>\n";
   	  applist += "	<thead>\n";
   	  applist += "		<tr>\n"; //class=\"list-header\"
   	  applist += "			<td style=\"padding:10px;background-color:#ff6384;font-weight:bold\" colspan=6>Billing Info</td>\n";
   	  applist += "		</tr>\n";
   	  applist += "			<th>Visit</th><th>Visit Type</th><th>Doctor</th><th>Bill Date</th><th>Amount</th><th>Note</th>\n";
   	  applist += "		</thead>\n";
   	  applist += "		<tbody>\n";
      
	  applist += "			<tr><td>1</td><td>OPD</td><td>Dr. Syed  Sayed Ahmed</td><td>01-Jan-2013</td><td>3,000.00</td><td></td></tr>\n";
	  applist += "			<tr><td>2</td><td>OPD</td><td>Dr. Syed  Sayed Ahmed</td><td>02-Jan-2013</td><td>1,500.00</td><td></td></tr>\n";
	  applist += "			<tr><td>3</td><td>IPD</td><td>Dr. Syed  Sayed Ahmed</td><td>04-Jan-2013</td><td>1,50,000.00</td><td></td></tr>\n";
	  applist += "			<tr><td>4</td><td>OPD</td><td>Dr. Syed  Sayed Ahmed</td><td>10-Jan-2013</td><td>2,000.00</td><td></td></tr>\n";
	  applist += "			<tr><td>5</td><td>OPD</td><td>Dr. Syed  Sayed Ahmed</td><td>22-Jan-2015</td><td>23,437.00</td><td></td></tr>\n";
	  applist += "			<tr><td>6</td><td>IPD</td><td>Dr. Syed  Sayed Ahmed</td><td>25-Feb-2015</td><td>3,56,345.00</td><td></td></tr>\n";
   	  applist += "		</tbody>";
   	  applist += "</table>";
   	  
   	  
   	  
   	  	s  =   "<table id=\"table-cap-filter-graph\" align=center valign=top border=0 cellpadding=10 cellspacing=10 width=99% height=100%>\n";
	    s +=   "	<tr>\n";
	    s +=   "		<td colspan=2 align=left>\n";
	    s +=   "			<div style=\"padding:10px;height:50px;background-color:#fff;box-shadow:1px 1px 2px rgba(0, 0, 0, 0.4);font-size:20px\">Patient ID: "+docname+", Name: "+desig+", Department: "+deptname+"</div>\n";
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n";
   	  	s +=   "	<tr>\n";
	  	s +=   "		<td align=left valign=top width=70%>\n";
	  	
	  	//************************ 1st Row ************************************//
	  	
	    
	    s +=   "						<table id=\"table-hs-2nd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% height=100%>\n";
	    s +=   "							<tr>\n"; 
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"font-size:13px;margin-right:10px;background-color:#fff;\">\n";
	    s +=   applist;
	    s +=   "									</div>\n";
	    s +=   "	 							</td>\n";
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"font-size:13px;background-color:#fff;\">\n";
	    s +=   otsch;
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    /*
	    s +=   "								<td width=32% valign=top>\n";
	    s +=   "									<div style=\"font-size:13px;margin-right:10px;background-color:#fff;\">\n";
	    s +=   deptwiseipdinfo;
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    */
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow


	    //************************ end 1st Row ************************************//

	    
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    s +=   "				<tr>\n"; // 3rd  tr with graph
	    s +=   "					<td colspan=4 class=\"hs-info-td\">\n";
	    
	    //********************** 2nd row ***************************************//

	  	s +=   "			<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; // 2nd  tr with graph
	    //s +=   "					<td>\n";
	    //s +=   "						<table id=\"table-hs-3rd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% >\n";
	    //s +=   "							<tr>\n"; 
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"font-size:13px;background-color:#fff;\">\n";
	    s +=   testinfo;
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"font-size:13px;background-color:#fff;\">\n";
	    s +=   rtestinfo;
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow
	    
	    //********************** end 2nd row ***************************************//
	    
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    //s +=   "			</td>\n";  // end of 80% td
	    //s +=   "		</tr>\n"; 
	    //s +=   "	</table>\n"; 
	    s +=   "</td>\n"; // end of 80% td
	    s +=   "	</tr>\n";
	    s +=   "</table>\n";
	    
	    s +=   "			<script>\n";
	    s +=   "var config = {\n";
	    s +=   "		type: 'line',\n";
	    s +=   "		data: {\n";
	    s +=   "			labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'OPD',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				data: [300,200,270,260,250,310,300,0,0,0,0,0],\n";
	    s +=   "				fill: false,\n";
	    s +=   "			}, {\n";
	    s +=   "				label: 'IPD',\n";
	    s +=   "				fill: false,\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				data: [120,130,160,200,50,110,60,0,0,0,0,0],\n";
	    s +=   "			}]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Month Wise OPD/IPD Info'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Patient Count'\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";
	    
	    s +=   "var ipdconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Procedure Done',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				data: [151,106,176,143,152,131,134,0,0,0,0,0],\n";
	    s +=   "				fill: false,\n";
	    s +=   "			}]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Month Wise Procedure Info'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Patient Count'\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";
	    s +=   "	window.onload = function() {\n";
	    s +=   "		var ctx = document.getElementById('mwopd').getContext('2d');\n";
	    s +=   "		var chart = new Chart(ctx, config);\n";
	    s +=   "		var ctx1 = document.getElementById('mwipd').getContext('2d');\n";
	    s +=   "		var chart1 = new Chart(ctx1, ipdconfig);\n";
	    /*
	    s +=   "		var ctx2 = document.getElementById('catcussales').getContext('2d');\n";
	    s +=   "		var chart2 = new Chart(ctx2, catcussalesconfig);\n";
	    s +=   "		var ctx3 = document.getElementById('territorysales').getContext('2d');\n";
	    s +=   "		var chart3 = new Chart(ctx3, territoryconfig);\n";
	    s +=   "		var ctx4 = document.getElementById('spsales').getContext('2d');\n";
	    s +=   "		var chart4 = new Chart(ctx4, spsalesconfig);\n";
	    */
	    s +=   "	};\n";
	    
	    s +=   "							</script>\n";
	    //s +=   "						</div>\n";

	  return s;
  }

  public static String gulmanagement(zabInfo zabinfo){
	  String s = "";
	  String caption = "Sales";
	  String name = "Sales";
	  int per = 3;
	  int year = 2020;
	  String label = "labels:['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']";
	  String [] period = {"1","2","3","4","5","6","7","8","9","10","11","12"};
	  String [] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	  String data = "";
	  String id = ""+zabinfo.ses.getAttribute("id");
	  String email = ""+zabinfo.ses.getAttribute("email");
	  String sp = "zabsp_Gunze_hsDashboard_Sales "+id+",'"+email+"','"+name+"',"+year;

	  //**** executing stored produre
	  //zabinfo.executeSP(sp);
   	  
   	  
   	  	s =   "<table id=\"table-cap-filter-graph\" align=center valign=top border=0 cellpadding=10 cellspacing=10 width=99% height=100%>\n";
	    s +=   "	<tr>\n";
	    s +=   "		<td colspan=2 align=left>\n";
//	    s +=   "			<div style=\"padding:10px;height:50px;background-color:#fff;box-shadow:1px 1px 2px rgba(0, 0, 0, 0.4);font-size:20px\">"+docname+", "+desig+", "+deptname+"</div>\n";
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n";

	    //************************ 1st Row ************************************//
/*
	    s +=   "	<tr>\n";
	  	s +=   "		<td align=left valign=top width=70%>\n";
	  	
	  	
	    s +=   "						<table id=\"table-hs-2nd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% height=100%>\n";
	    s +=   "							<tr>\n"; 
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"font-size:13px;margin-right:10px;background-color:#fff;\">\n";
	    s +=   applist;
	    s +=   "									</div>\n";
	    s +=   "	 							</td>\n";
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"font-size:13px;background-color:#fff;\">\n";
	    s +=   otsch;
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow



	    
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
*/
	    //************************ end 1st Row ************************************//

	    //********************** 2nd row ***************************************//
	    s +=   "				<tr>\n"; // 3rd  tr with graph
	    s +=   "					<td colspan=4 class=\"hs-info-td\">\n";
	    

	  	s +=   "			<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; // 2nd  tr with graph
	    //s +=   "					<td>\n";
	    //s +=   "						<table id=\"table-hs-3rd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% >\n";
	    //s +=   "							<tr>\n"; 
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"sales\" height=160px></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"arled\" height=\"160px\"></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow
	    
	    
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    //********************** end 2nd row ***************************************//

	    //********************** 3rd row ***************************************//
	    s +=   "				<tr>\n"; // 3rd  tr with graph
	    s +=   "					<td colspan=4 class=\"hs-info-td\">\n";
	    

	  	s +=   "			<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; // 2nd  tr with graph
	    //s +=   "					<td>\n";
	    //s +=   "						<table id=\"table-hs-3rd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% >\n";
	    //s +=   "							<tr>\n"; 
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"ordprddel\" height=160px></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"profitloss\" height=\"160px\"></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow
	    
	    
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    //********************** end 3rd row ***************************************//

	    //s +=   "			</td>\n";  // end of 80% td
	    //s +=   "		</tr>\n"; 
	    //s +=   "	</table>\n"; 
	    s +=   "</td>\n"; // end of 80% td
	    s +=   "	</tr>\n";
	    s +=   "</table>\n";
	    
	      sql = "select xper,sum(xlineamt) from opsalessum";
	      sql += " where zid='"+id+"'";
	      sql += " and xyear="+year;
	      sql += " and xtype='order'";
	      sql += " and xgcus='Local'";
	      sql += " group by xper";
	      sql += " order by xper";
	   	  resultrows = zabinfo.getSqlRows(sql);
	//System.err.println(resultrows+" "+sql);
	   	  if(resultrows==null){
	   		  data = "data: []";
	   	  }else{
	   		  for(int i=0;i<resultrows.length;i++) {	
	   			  //label += "'"+resultrows[i][0]+"',";
	   			  data += resultrows[i][1]+",";
	   		  }
	   		 // label = label.substring(0,label.length()-1);
	   		 // label = "labels:["+label+"]";
	   		  data = data.substring(0,data.length()-1);
	   		  data = "data:["+data+"]";
	   	  }
		    
	    s +=    "	<script>\n";
	    s +=   "var salesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
//	    s +=   "			labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Local Sales',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
//	    s +=   "				data: [151,106,176,143,152,131,134,0,0,0,0,0],\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},{\n";
	    
	    label = "";
	    data = "";
	    sql = "select xper,SUM(xlineamt) from opsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+year;
	    sql += " and xtype='order'";
	    sql += " and xgcus='Foreign'";
	    sql += " group by xper";
	    sql += " order by xper";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
	   		  data = "data: []";
	   	}else{
	   		  for(int i=0;i<resultrows.length;i++) {	
	   			  label += "'"+resultrows[i][0]+"',";
	   			  data += resultrows[i][1]+",";
	   		  }
	   		  label = label.substring(0,label.length()-1);
	   		  label = "labels:["+label+"]";
	   		  data = data.substring(0,data.length()-1);
	   		  data = "data:["+data+"]";
	   	  }

	    s +=   "				label: 'Foreign Sales',\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Month Wise Sales for the Year:"+year+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Amount($) '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    label = "";
	    data = "";
	      sql = "select xlabel,xdata from hssales";
	      sql += " where zid='"+id+"' ";
	      sql += "and xname='arled'";
	      sql += " order by xrow";
	   	  resultrows = zabinfo.getSqlRows(sql);
	   	  if(resultrows==null){
	   		  data = "data: []";
	   	  }else{
	   		  for(int i=0;i<resultrows.length;i++) {	
	   			  label += "'"+resultrows[i][0]+"',";
	   			  data += resultrows[i][1]+",";
	   		  }
	   		  label = label.substring(0,label.length()-1);
	   		  label = "labels:["+label+"]";
	   		  data = data.substring(0,data.length()-1);
	   		  data = "data:["+data+"]";
	   	  }
	   	  
	    s +=   "var arledconfig = {\n";
	    s +=   "		type: 'line',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Recevable',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},{\n";
	    
	    label = "";
	    data = "";
	      sql = "select xlabel,xdata from hssales";
	      sql += " where zid='"+id+"' ";
	      sql += "and xname='arcoll'";
	      sql += " order by xrow";
	   	  resultrows = zabinfo.getSqlRows(sql);
	   	  if(resultrows==null){
	   		  data = "data: []";
	   	  }else{
	   		  for(int i=0;i<resultrows.length;i++) {	
	   			  label += "'"+resultrows[i][0]+"',";
	   			  data += resultrows[i][1]+",";
	   		  }
	   		  label = label.substring(0,label.length()-1);
	   		  label = "labels:["+label+"]";
	   		  data = data.substring(0,data.length()-1);
	   		  data = "data:["+data+"]";
	   	  }

	    s +=   "				label: 'Collection',\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Month Wise Receivable & Collection for the Year:"+year+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Amount($) '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	   	// MONTH WISE ORDER PRODUCTION AND DELIVERY  \\

	    data = "";
	    sql = "select xper,sum(xqtykg) from opsalessum";
	    sql += " where zid='"+id+"' ";
	    sql += " and xyear="+year;
	    sql += " and xtype='order'";
	    sql += " group by xper";
	    sql += " order by xper";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
			data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  data += resultrows[i][1]+",";
	   	  }
	   	  data = data.substring(0,data.length()-1);
	   	  data = "data:["+data+"]";
	   	}
	    
	   	
	    s +=   "var ordprddelconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Sales Order',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},{\n";
	    
	    //label = "";
	    data = "";
	    sql = "select xper,sum(xqtykg) from opprdsum";
	    sql += " where zid='"+id+"' ";
	    sql += " and xyear="+year;
	    sql += " and xtype='Inspection'";
	    sql += " group by xper";
	    sql += " order by xper";
	   	resultrows = zabinfo.getSqlRows(sql);
//	   	System.err.println(sql);
	   	if(resultrows==null){
			data = "data: []";
	   	}else{
	   		for(int i=0;i<resultrows.length;i++) {	
	   			  data += resultrows[i][1]+",";
	   		  }
	   		  data = data.substring(0,data.length()-1);
	   		  data = "data:["+data+"]";
	   	}

	    s +=   "				label: 'Production',\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},{\n";
		    
	    data = "";
	    sql = "select xper,sum(xqtykg) from opprdsum";
	    sql += " where zid='"+id+"' ";
	    sql += " and xyear="+year;
	    sql += " and xtype='Delivery'";
	    sql += " group by xper";
	    sql += " order by xper";
		resultrows = zabinfo.getSqlRows(sql);
		if(resultrows==null){
			data = "data: []";
		}else{
			for(int i=0;i<resultrows.length;i++) {	
	   			  data += resultrows[i][1]+",";
   		  	}
   		  	data = data.substring(0,data.length()-1);
		   	data = "data:["+data+"]";
		}
	    s +=   "				label: 'Delivery',\n";
	    s +=   "				backgroundColor: window.chartColors.grey,\n";
	    s +=   "				borderColor: window.chartColors.grey,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				text: 'Month Wise Sales Order,Production and Delivery for the Year:"+year+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Qunatity(Kg) '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";
	    
	    //double pl = 0.00;
	    //BigDecimal val = new BigDecimal(0);
	    BigDecimal pl = new BigDecimal(0);
	    label = "";
	    data = "";		
	    sql = "select hrc2.xdesc,sum(xprime) from acbal a";
	    sql += " join acmst b"; 
	    sql += " on a.zid=b.zid";
	    sql += " and a.xacc=b.xacc";
	    sql += " join achrc2 hrc2";
	    sql += " on b.zid=hrc2.zid";
	    sql += " and b.xhrc1=hrc2.xhrc1";
	    sql += " and b.xhrc2=hrc2.xhrc2";
	    sql += " and xyear="+year;
	    sql += " and b.xacctype in('Income','Expenditure')";
	    sql += " group by hrc2.xdesc";
	    
	    resultrows = zabinfo.getSqlRows(sql);
		if(resultrows==null){
			label = "labels:[]";
			data = "data: []";
		}else{
			for(int i=0;i<resultrows.length;i++) {
				pl = pl.add(zabTools.getBigDecimal(resultrows[i][1]));
				label += "'"+resultrows[i][0]+"',";
	   			data += resultrows[i][1]+",";
   		  	}
   		  	label = label.substring(0,label.length()-1);
   		  	data = data.substring(0,data.length()-1);
		   	data = "data:["+data+"]";
		   	label = "labels:["+label+"]";
		}
		//System.err.println(pl.compareTo(BigDecimal.ZERO));
		//if(pl>0)
		//	pl = pl/1000000;
		s +=   "var plconfig = {\n";
	    s +=   "		type: 'pie',\n";
	    s +=   "		data: {\n";
	    //s +=   "		labels: ['Bingo','bunty','bunty1','bunty2','bunty3'],\n";
	    s +=   "			datasets: [{\n";
	    s +=   data+",\n";
	    //s +=   "				data: [10,20,34,45,76\n";
	    //s +=   "				],\n";
	    s +=   "				backgroundColor: [\n";
	    s +=   "					window.chartColors.red,\n";
	    s +=   "					window.chartColors.orange,\n";
	    s +=   "					window.chartColors.yellow,\n";
	    s +=   "					window.chartColors.green,\n";
	    s +=   "					window.chartColors.blue,\n";
	    s +=   "				],\n";
	    s +=   "				label: 'Dataset 1'\n";
	    s +=   "			}],\n";
	    s +=   label+"\n";
	    //s +=   "			labels: [\n";
	    //s +=   "				'Red1',\n";
	    //s +=   "				'Orange',\n";
	    //s +=   "				'Yellow',\n";
	    //s +=   "				'Green',\n";
	    //s +=   "				'Blue'\n";
	    //s +=   "			]\n";
	    
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
/*	    
	    if(pl.compareTo(BigDecimal.ZERO)<0)
	    	s +=   "				text: 'Profit & Loss Statement As on:"+zabTools.dateDisplay(new java.sql.Date(System.currentTimeMillis()).toString(), 'D', '-')+", Profit: "+pl.divide(zabTools.getBigDecimal("1000000")).round(new MathContext(2))+", Million'\n";
	    else
	    	s +=   "				text: 'Profit & Loss Statement As on:"+zabTools.dateDisplay(new java.sql.Date(System.currentTimeMillis()).toString(), 'D', '-')+", Loss: "+pl.divide(zabTools.getBigDecimal("1000000")).round(new MathContext(2))+", Million'\n";
*/	    	
//	    s +=   "    					callback: function(value) {\n";
//	    s +=   "        					return addCommas(value);\n";
//	    s +=   "    					}\n";

	    if(pl.compareTo(BigDecimal.ZERO)<0)
	    	s +=   "				text: 'Profit & Loss Statement As on:"+zabTools.dateDisplay(new java.sql.Date(System.currentTimeMillis()).toString(), 'D', '-')+", Profit: "+pl+"'\n";
	    else
	    	s +=   "				text: 'Profit & Loss Statement As on:"+zabTools.dateDisplay(new java.sql.Date(System.currentTimeMillis()).toString(), 'D', '-')+", Loss: "+pl+"'\n";
	    
	    s +=   "			},\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

		s +=   "	window.onload = function() {\n";
	    s +=   "		var ctx = document.getElementById('sales').getContext('2d');\n";
	    s +=   "		var chart = new Chart(ctx, salesconfig);\n";
	    s +=   "		var ctx1 = document.getElementById('arled').getContext('2d');\n";
	    s +=   "		var chart1 = new Chart(ctx1, arledconfig);\n";
	    s +=   "		var ctx2 = document.getElementById('ordprddel').getContext('2d');\n";
	    s +=   "		var chart2 = new Chart(ctx2, ordprddelconfig);\n";
	    s +=   "		var ctx3 = document.getElementById('profitloss').getContext('2d');\n";
	    s +=   "		var chart3 = new Chart(ctx3, plconfig);\n";
	    s +=   "	};\n";
	    
	    s +=   "							</script>\n";

	  return s;
  }
  
  public static String unimartMgnCusCount(zabInfo zabinfo){
	  
	  String s = "";
	  String caption = "Sales";
	  String name = "Sales";
	  
	  LocalDate today = LocalDate.of(2020,8,24);  //java.time.LocalDate.now();
	  LocalDate firstDateOfLastMonth = LocalDate.of(2020,7,1);
	  LocalDate lastDateOfLastMonth = LocalDate.of(2020,7,31);
	  long noOfDaysLastMonth = 31;
	  LocalDate firstDateOfLast2Month = LocalDate.of(2020,6,1);
	  LocalDate lastDateOfLast2Month = LocalDate.of(2020,6,30);
	  long noOfDaysLast2Month = 30;
/*
	  LocalDate today = java.time.LocalDate.now();
	  
	  LocalDate firstDateOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
	  LocalDate lastDateOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
	  long noOfDaysLastMonth = ChronoUnit.DAYS.between(firstDateOfLastMonth,lastDateOfLastMonth);

	  LocalDate firstDateOfLast2Month = today.minusMonths(2).with(TemporalAdjusters.firstDayOfMonth());
	  LocalDate lastDateOfLast2Month = today.minusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
	  long noOfDaysLast2Month = ChronoUnit.DAYS.between(firstDateOfLast2Month,lastDateOfLast2Month);
*/	  
	  LocalDate prvdate = today.minusDays(1);
	  int year = today.getYear();
	  Month lastmonth = today.getMonth().minus(1);

	  LocalDate firstdate = today.with(TemporalAdjusters.firstDayOfMonth());
	  long noOfDaysCurMonth = ChronoUnit.DAYS.between(firstdate, today);
	  
	  BigDecimal amount = new BigDecimal("0");
	  BigDecimal totamt = new BigDecimal("0");
	  
	  String label = "";
	  String data = "";
	  String id = ""+zabinfo.ses.getAttribute("id");
	  
	    //************************ 1st Row ************************************//


	    s =   "	<div id=\"ccModal\" class=\"modal fade\" tabindex=\"-1\" role=\"dialog\" aria-hidden=\"true\">\n";
		s +=  "		<div class=\"modal-dialog \" aria-labelledby=\"ccModalTitle\" role=\"document\"></div>\n";

	    //<!-- Modal content-->
		s += " 			<div class=\"modal-content\" >\n";
		s += " 				<div class=\"modal-header\" >\n";
		s += " 					<h5 class=\"modal-title\" id=\"ccModalTitle\">Customer Count</h5>\n";
		s += "					<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n";
		s += "  					<span aria-hidden=\"true\">&times;</span>\n";
		s += "					</button>\n";
		s += "				</div>\n"; 
		s += "				<div class=\"modal-body\">\n";
   	  	s += "					<table id=\"table-cap-filter-graph\" align=center valign=top border=0 cellpadding=10 cellspacing=10 width=99% height=100%>\n";
	    s += "						<tr>\n"; // 3rd  tr with graph
	    s += "							<td colspan=4 class=\"hs-info-td\">\n";
	  	s += "								<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s += "									<tr>\n"; 
	    s += "										<td width=33% valign=top>\n";
	    s += "											<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s += "												<canvas id=\"lastdayssalescc\" height=160px></canvas>\n";
	    s += "											</div>\n";
	    s += "										</td>\n";
	    
	    s += "										<td width=33% valign=top>\n";
	    s += "											<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s += "												<canvas id=\"mtdsalescc\" height=\"160px\"></canvas>\n";
	    s += "											</div>\n";
	    s += "										</td>\n";
	    
	    s +=   "									<td width=33% valign=top>\n";
	    s +=   "										<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "											<canvas id=\"mtdavgsalescc\" height=\"160px\"></canvas>\n";
	    s +=   "										</div>\n";
	    s +=   "									</td>\n";
	    s +=   "								</tr>\n";
	    s +=   "							</table>\n"; 
	    s +=   "						</td>\n";
	    s +=   "					</tr>\n";
	    s +=   "				</table>\n"; 
		s += "				</div>\n"; 
		s += "			</div>\n"; 
		s += "		</div>\n"; 
	    
		//////// Average Basket Value Modal \\\\\\
	    s +=   "	<div id=\"abvModal\" class=\"modal fade\" tabindex=\"-2\" role=\"dialog\" aria-hidden=\"true\">\n";
		s +=  "		<div class=\"modal-dialog \" aria-labelledby=\"abvModalTitle\" role=\"document\"></div>\n";

	    //<!-- Modal content-->
		s += " 			<div class=\"modal-content\" >\n";
		s += " 				<div class=\"modal-header\" >\n";
		s += " 					<h5 class=\"modal-title\" id=\"abvModalTitle\">Average Basket Value</h5>\n";
		s += "					<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n";
		s += "  					<span aria-hidden=\"true\">&times;</span>\n";
		s += "					</button>\n";
		s += "				</div>\n"; 
		s += "				<div class=\"modal-body\">\n";
   	  	s += "					<table id=\"table-cap-filter-graph\" align=center valign=top border=0 cellpadding=10 cellspacing=10 width=99% height=100%>\n";
	    s += "						<tr>\n"; // 3rd  tr with graph
	    s += "							<td colspan=4 class=\"hs-info-td\">\n";
	  	s += "								<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s += "									<tr>\n"; 
	    s += "										<td width=33% valign=top>\n";
	    s += "											<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s += "												<canvas id=\"lastdayssalescc\" height=160px></canvas>\n";
	    s += "											</div>\n";
	    s += "										</td>\n";
	    
	    s += "										<td width=33% valign=top>\n";
	    s += "											<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s += "												<canvas id=\"mtdsalescc\" height=\"160px\"></canvas>\n";
	    s += "											</div>\n";
	    s += "										</td>\n";
	    
	    s +=   "									<td width=33% valign=top>\n";
	    s +=   "										<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "											<canvas id=\"mtdavgsalescc\" height=\"160px\"></canvas>\n";
	    s +=   "										</div>\n";
	    s +=   "									</td>\n";
	    s +=   "								</tr>\n";
	    s +=   "							</table>\n"; 
	    s +=   "						</td>\n";
	    s +=   "					</tr>\n";
	    s +=   "				</table>\n"; 
		s += "				</div>\n"; 
		s += "			</div>\n"; 
		s += "		</div>\n"; 
	    
		
		
//	    s +=   "					</td>\n";
//	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	  return s;
  }

  public static String unimartmanagement_old(zabInfo zabinfo){
	  
	  String s = "";
	  String caption = "Sales";
	  String name = "Sales";
	  
	  LocalDate today = LocalDate.of(2020,8,24);  //java.time.LocalDate.now();
	  LocalDate firstDateOfLastMonth = LocalDate.of(2020,7,1);
	  LocalDate lastDateOfLastMonth = LocalDate.of(2020,7,31);
	  long noOfDaysLastMonth = 31;
	  LocalDate firstDateOfLast2Month = LocalDate.of(2020,6,1);
	  LocalDate lastDateOfLast2Month = LocalDate.of(2020,6,30);
	  long noOfDaysLast2Month = 30;
/*
	  LocalDate today = java.time.LocalDate.now();
	  
	  LocalDate firstDateOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
	  LocalDate lastDateOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
	  long noOfDaysLastMonth = ChronoUnit.DAYS.between(firstDateOfLastMonth,lastDateOfLastMonth);

	  LocalDate firstDateOfLast2Month = today.minusMonths(2).with(TemporalAdjusters.firstDayOfMonth());
	  LocalDate lastDateOfLast2Month = today.minusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
	  long noOfDaysLast2Month = ChronoUnit.DAYS.between(firstDateOfLast2Month,lastDateOfLast2Month);
*/	  
	  LocalDate prvdate = today.minusDays(1);
//	  int period = today.getMonthValue();
	  int year = today.getYear();
//	  int yearOfLastMonth = year;
	  Month lastmonth = today.getMonth().minus(1);

	  LocalDate firstdate = today.with(TemporalAdjusters.firstDayOfMonth());
	  long noOfDaysCurMonth = ChronoUnit.DAYS.between(firstdate, today);
	  
	  BigDecimal amount = new BigDecimal("0");
	  BigDecimal totamt = new BigDecimal("0");
	  

	  //String today = java.time.LocalDate.now().toString();
	  //String prvdate = java.time.LocalDate.now().minusDays(1).toString();
	  //int per = java.time.LocalDate.now().getMonthValue();
//	  int year = java.time.LocalDate.now().getYear();
//	  System.err.println(java.time.LocalDate.now().getMonthValue());
//	  System.err.println(java.time.LocalDate.now().getMonth());

//	  int period = today.getMonthValue();
	  //int year = 2020;
	  //String today = "2020-08-24";
	  //String prvdate = "2020-08-23";
	  //String firstdate = year+"-08-01";   

	  String label = "";
	  //String [] period = {"1","2","3","4","5","6","7","8","9","10","11","12"};
	  String data = "";
	  String id = ""+zabinfo.ses.getAttribute("id");
	  
//	  String sp = "zabsp_Gunze_hsDashboard_Sales "+id+",'"+email+"','"+name+"',"+year;

	  //**** executing stored produre
	  //zabinfo.executeSP(sp);
   	  
   	  
   	  	s =   "<table id=\"table-cap-filter-graph\" align=center valign=top border=0 cellpadding=10 cellspacing=10 width=99% height=100%>\n";
	    s +=   "	<tr>\n";
	    s +=   "		<td colspan=2 align=left>\n";
//	    s +=   "			<div style=\"text-align:left;padding:10px;height:75px;background-color:skyblue;box-shadow:1px 1px 2px rgba(0, 0, 0, 0.4);font-size:20px;font-weight:bold;\">Net Sales<br>Date:"+zabTools.dateDisplay(today.toString(), 'D', '-')+"\n";
	    s +=   "			<div style=\"text-align:left;padding:10px;height:75px;background-color:skyblue;box-shadow:1px 1px 2px rgba(0, 0, 0, 0.4);font-size:20px;font-weight:bold;\">\n";
	    s +=   "				<button type=\"button\" class=\"btn btn-info btn-lg\" data-toggle=\"modal\" data-target=\"#ccModal\">Customer Count</button>\n";
	    s +=   "				<button type=\"button\" class=\"btn btn-info btn-lg\" data-toggle=\"modal\" data-target=\"#abvModal\">Average Basket Value</button>\n";
	    s +=   "			</div>\n";
		s +=   unimartMgnCusCount(zabinfo);
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n";

	    
	    //************************ 1st Row ************************************//

	    s +=   "				<tr>\n"; // 3rd  tr with graph
	    s +=   "					<td colspan=4 class=\"hs-info-td\">\n";
//		s +=   unimartMgnCusCount(zabinfo);
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph

	    s +=   "				<tr>\n"; 
	    s +=   "					<td colspan=4 class=\"hs-info-td\">\n";

	  	s +=   "			<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; // 2nd  tr with graph
	    //s +=   "					<td>\n";
	    //s +=   "						<table id=\"table-hs-3rd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% >\n";
	    //s +=   "							<tr>\n"; 
	    s +=   "								<td width=33% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"lastdayssales\" height=160px></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    
	    s +=   "								<td width=33% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"mtdsales\" height=\"160px\"></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    
	    s +=   "								<td width=33% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"mtdavgsales\" height=\"160px\"></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow
	    
	    
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    //************************ end 1st Row ************************************//

	    //********************** 2nd row ***************************************//
	    s +=   "				<tr>\n"; // 3rd  tr with graph
	    s +=   "					<td colspan=4 class=\"hs-info-td\">\n";
	    

	  	s +=   "			<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; // 2nd  tr with graph
	    //s +=   "					<td>\n";
	    //s +=   "						<table id=\"table-hs-3rd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% >\n";
	    //s +=   "							<tr>\n"; 
	    
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"lmsales\" height=\"160px\"></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"lmavgsales\" height=\"160px\"></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow
	    
	    
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    //********************** end 2nd row ***************************************//
	    //********************** 3rd row ***************************************//
	    s +=   "				<tr>\n"; // 3rd  tr with graph
	    s +=   "					<td colspan=4 class=\"hs-info-td\">\n";
	    

	  	s +=   "			<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; // 2nd  tr with graph
	    //s +=   "					<td>\n";
	    //s +=   "						<table id=\"table-hs-3rd-row\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100% >\n";
	    //s +=   "							<tr>\n"; 
	    
	    
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"lm2sales\" height=\"160px\"></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    
	    s +=   "								<td width=50% valign=top>\n";
	    s +=   "									<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "										<canvas id=\"lm2avgsales\" height=\"160px\"></canvas>\n";
	    s +=   "									</div>\n";
	    s +=   "								</td>\n";
	    
	    s +=   "							</tr>\n"; // end tr table-hs-2ndrow
	    s +=   "						</table>\n"; // end table-hs-2ndrow
	    
	    
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; // end 2nd  tr with graph
	    //********************** end 3rd row ***************************************//

	    //s +=   "			</td>\n";  // end of 80% td
	    //s +=   "		</tr>\n"; 
	    //s +=   "	</table>\n"; 
	    s +=   "</td>\n"; // end of 80% td
	    s +=   "	</tr>\n";
	    s +=   "</table>\n";
	    
	    //*****************GRAPH FOR LAST DAYS SALES ********************************//
	    label = "";
	    sql = "select xdiv,xlineamt from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xdate='"+prvdate.toString()+"'";
	    sql += "order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0)
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(2));
	   		  else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  //label = label.substring(0,label.length()-1);
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  //data = data.substring(0,data.length()-1);
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
		    
	    s +=   "	<script>\n";
	    s +=   "var lastdaysnetsalesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Sales',\n";
// 	    s +=   "				backgroundColor: [\"red\", \"red\", \"yellow\", \"yellow\", \"blue\"],\n";
 	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Last Days Net Sales'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Amount(Million) '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    //*****************END GRAPH FOR LAST DAYS SALES ********************************//
	    
	    //***************** GRAPH FOR MTD SALES ********************************//
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0);
	    totamt = new BigDecimal(0);
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xdate between '"+firstdate.toString()+"' and '"+today.toString()+"'";
	    sql += "group by xdiv ";
	    sql += "order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0)
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(2));
	   		  else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  //label = label.substring(0,label.length()-1);
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  //data = data.substring(0,data.length()-1);
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
		    
	    
	    s +=   "var mtdsalesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Local Sales',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'MTD Sales'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Amount(Million) '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    //***************** END GRAPH FOR MTD SALES ********************************//
	    
	    
	    //***************** GRAPH FOR MTD AVG SALES ********************************//
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xdate between '"+firstdate.toString()+"' and '"+today.toString()+"'";
	    sql += "group by xdiv ";
	    sql += "order by xdiv";
	    System.err.println(sql);
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  //amount = amount.divide(new BigDecimal("23").round(new MathContext(2))); 
	   			  amount = amount.divide(new BigDecimal(noOfDaysCurMonth),2,RoundingMode.HALF_UP); 
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(3));
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
		    
	    
	    s +=   "var mtdavgsalesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Local Sales',\n";
	    s +=   "				backgroundColor: window.chartColors.yellow,\n";
	    s +=   "				borderColor: window.chartColors.yellow,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'MTD Average Sales'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Amount(Million) '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    //******************* END GRAPH FOR MTD AVG SALES ********************************//

	    //********************* GRAPH FOR LAST MONTH NET SALES ********************************//
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xdate between '"+firstDateOfLastMonth.toString()+"' and '"+lastDateOfLastMonth.toString()+"'";
	    sql += "group by xdiv ";
	    sql += "order by xdiv";
	    System.err.println(sql);
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0)
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(2));
	   		  else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  //label = label.substring(0,label.length()-1);
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  //data = data.substring(0,data.length()-1);
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
		    
	    
	    s +=   "var lmsalesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Local Sales',\n";
	    s +=   "				backgroundColor: window.chartColors.orange,\n";
	    s +=   "				borderColor: window.chartColors.orange,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Net Sales of "+lastmonth+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Amount '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    //********************* END GRAPH FOR LAST MONTH NET SALES ********************************//

	    
	    //********************* GRAPH FOR DAILY AVG OF LAST MONTH SALES********************************//
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xdate between '"+firstDateOfLastMonth.toString()+"' and '"+lastDateOfLastMonth.toString()+"'";
	    sql += "group by xdiv ";
	    sql += "order by xdiv";
	    System.err.println(sql);
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  System.err.println(amount+" noofodsays "+noOfDaysLastMonth);
	   			  amount = amount.divide(new BigDecimal(noOfDaysLastMonth),2,RoundingMode.CEILING); 
	   			  System.err.println("after "+amount);
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(3));
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
		    
	    
	    s +=   "var lmavgsalesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Local Sales',\n";
//	    s +=   "				backgroundColor: [\"red\", \"blue\", \"green\", \"yellow\"],\n";
 	    s +=   "				backgroundColor: window.chartColors.green,\n";
	    s +=   "				borderColor: window.chartColors.green,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Daily Average Sales of "+lastmonth+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Amount(Million) '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    //********************* END GRAPH FOR DAILY AVG OF LAST MONTH ********************************//

	    //********************* GRAPH FOR NET SALES LAST 2 MONTH ********************************//
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xdate between '"+firstDateOfLast2Month.toString()+"' and '"+lastDateOfLast2Month.toString()+"'";
	    sql += "group by xdiv ";
	    sql += "order by xdiv";
	    System.err.println(sql);
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0)
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(2));
	   		  else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  //label = label.substring(0,label.length()-1);
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  //data = data.substring(0,data.length()-1);
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    
	    s +=   "var lm2salesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Sales',\n";
	    s +=   "				backgroundColor: window.chartColors.black,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				borderWidth: 1,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Net Sales of "+lastmonth.minus(1)+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Amount(Million) '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    //********************* END GRAPH FOR NET SALES LAST MONTH ********************************//

	    
	    //********************* GRAPH FOR DAILY AVG OF LAST MONTH ********************************//
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xdate between '"+firstDateOfLast2Month.toString()+"' and '"+lastDateOfLast2Month.toString()+"'";
	    sql += "group by xdiv ";
	    sql += "order by xdiv";
	    System.err.println(sql);
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(new BigDecimal(noOfDaysLast2Month),2,RoundingMode.HALF_UP); 
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(3));
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
		    
	    
	    s +=   "var lm2avgsalesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [{\n";
	    s +=   "				label: 'Local Sales',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Daily Average Sales of "+lastmonth.minus(1)+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Amount(Million) '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    //********************* END GRAPH FOR DAILY AVG OF LAST MONTH ********************************//

		s +=   "	window.onload = function() {\n";
	    s +=   "		var ctx = document.getElementById('lastdayssales').getContext('2d');\n";
	    s +=   "		var chart = new Chart(ctx, lastdaysnetsalesconfig);\n";
	    s +=   "		var ctx1 = document.getElementById('mtdsales').getContext('2d');\n";
	    s +=   "		var chart1 = new Chart(ctx1, mtdsalesconfig);\n";
	    s +=   "		var ctx2 = document.getElementById('mtdavgsales').getContext('2d');\n";
	    s +=   "		var chart2 = new Chart(ctx2, mtdavgsalesconfig);\n";
	    s +=   "		var ctx3 = document.getElementById('lmsales').getContext('2d');\n";
	    s +=   "		var chart3 = new Chart(ctx3, lmsalesconfig);\n";
	    s +=   "		var ctx4 = document.getElementById('lmavgsales').getContext('2d');\n";
	    s +=   "		var chart4 = new Chart(ctx4, lmavgsalesconfig);\n";
	    s +=   "		var ctx5 = document.getElementById('lm2sales').getContext('2d');\n";
	    s +=   "		var chart5 = new Chart(ctx5, lm2salesconfig);\n";
	    s +=   "		var ctx6 = document.getElementById('lm2avgsales').getContext('2d');\n";
	    s +=   "		var chart6 = new Chart(ctx6, lm2avgsalesconfig);\n";
	    s +=   "	};\n";
	    
	    s +=   "							</script>\n";

	  return s;
  }
  
  public static String unimartmanagement(zabInfo zabinfo){
	  
	  String s = "";
	  String caption = "Sales";
	  String name = "Sales";
	  
	  LocalDate today = LocalDate.of(2020,8,24);  //java.time.LocalDate.now();
	  LocalDate firstDateOfLastMonth = LocalDate.of(2020,7,1);
	  LocalDate lastDateOfLastMonth = LocalDate.of(2020,7,31);
	  long noOfDaysLastMonth = 31;
	  LocalDate firstDateOfLast2Month = LocalDate.of(2020,6,1);
	  LocalDate lastDateOfLast2Month = LocalDate.of(2020,6,30);
	  long noOfDaysLast2Month = 30;
/*
	  LocalDate today = java.time.LocalDate.now();
	  
	  LocalDate firstDateOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
	  LocalDate lastDateOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
	  long noOfDaysLastMonth = ChronoUnit.DAYS.between(firstDateOfLastMonth,lastDateOfLastMonth);

	  LocalDate firstDateOfLast2Month = today.minusMonths(2).with(TemporalAdjusters.firstDayOfMonth());
	  LocalDate lastDateOfLast2Month = today.minusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
	  long noOfDaysLast2Month = ChronoUnit.DAYS.between(firstDateOfLast2Month,lastDateOfLast2Month);
*/	  
	  LocalDate prvdate = today.minusDays(1);
//	  int period = today.getMonthValue();
	  int year = today.getYear();
//	  int yearOfLastMonth = year;
	  Month lastmonth = today.getMonth().minus(1);

	  LocalDate firstdate = today.with(TemporalAdjusters.firstDayOfMonth());
	  long noOfDaysCurMonth = ChronoUnit.DAYS.between(firstdate, today);
	  
	  BigDecimal amount = new BigDecimal("0");
	  BigDecimal totamt = new BigDecimal("0");
	  

	  //String today = java.time.LocalDate.now().toString();
	  //String prvdate = java.time.LocalDate.now().minusDays(1).toString();
	  //int per = java.time.LocalDate.now().getMonthValue();
//	  int year = java.time.LocalDate.now().getYear();
//	  System.err.println(java.time.LocalDate.now().getMonthValue());
//	  System.err.println(java.time.LocalDate.now().getMonth());

//	  int period = today.getMonthValue();
	  //int year = 2020;
	  //String today = "2020-08-24";
	  //String prvdate = "2020-08-23";
	  //String firstdate = year+"-08-01";   

	  String label = "";
	  //String [] period = {"1","2","3","4","5","6","7","8","9","10","11","12"};
	  String data = "";
	  String id = ""+zabinfo.ses.getAttribute("id");
	  
//	  String sp = "zabsp_Gunze_hsDashboard_Sales "+id+",'"+email+"','"+name+"',"+year;

	  //**** executing stored produre
	  //zabinfo.executeSP(sp);
   	  
   	  
   	  	s =   "<table id=\"hs-main-table\" align=center valign=top border=0 cellpadding=10 cellspacing=10 width=99% height=100%>\n";
	    s +=   "	<tr>\n";
	    s +=   "		<td colspan=2 align=left>\n";
//	    s +=   "			<div style=\"text-align:left;padding:10px;height:75px;background-color:skyblue;box-shadow:1px 1px 2px rgba(0, 0, 0, 0.4);font-size:20px;font-weight:bold;\">Net Sales<br>Date:"+zabTools.dateDisplay(today.toString(), 'D', '-')+"\n";
/*
	    s +=   "			<div style=\"text-align:left;padding:10px;height:75px;background-color:skyblue;box-shadow:1px 1px 2px rgba(0, 0, 0, 0.4);font-size:20px;font-weight:bold;\">\n";
	    s +=   "				<button type=\"button\" class=\"btn btn-info btn-lg\" data-toggle=\"modal\" data-target=\"#ccModal\">Customer Count</button>\n";
	    s +=   "				<button type=\"button\" class=\"btn btn-info btn-lg\" data-toggle=\"modal\" data-target=\"#abvModal\">Average Basket Value</button>\n";
	    s +=   "			</div>\n";
*/	    
	    s +=   "			<div class=\"hs-title\" >\n";
	    s +=   "				Net Sales<br>As of "+zabTools.dateDisplay(today.toString(), 'D', '-')+"\n";
	    s +=   "			</div>\n";
//		s +=   unimartMgnCusCount(zabinfo);
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n";

	    
	    //************************ 1st Row ************************************//

	    s +=   "	<tr>\n"; 
	    s +=   "		<td colspan=4>\n";
	  	s +=   "			<table align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; 
	  	
	    s +=   "					<td width=50% valign=top align=center>\n";
	    s +=   "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "							<canvas id=\"netsales\" height=160px></canvas>\n";
	    s +=   "							<small class=\"form-text text-capitalize text-muted\">(data captured from Sales Module)</small>";
	    s +=   "						</div>\n";
	    s +=   "					</td>\n";
	    
	    s +=   "					<td width=50% valign=top align=center>\n";
	    s +=   "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "							<canvas id=\"avgsales\" height=\"160px\"></canvas>\n";
	    s +=   "							<small class=\"form-text text-capitalize text-muted\">(data captured from Sales Module)</small>";
	    s +=   "						</div>\n";
	    s +=   "					</td>\n";
	    
	    s +=   "				</tr>\n"; 
	    s +=   "			</table>\n"; 
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n"; 
	    //************************ end 1st Row ************************************//

	    
	    //********************** 2nd row ***************************************//
	  	s +=   "	<tr>\n"; 
	    s +=   "		<td colspan=4 >\n";
	    s +=   "			<div class=\"hs-title\" >\n";
	    s +=   "				Customer Count<br>As of "+zabTools.dateDisplay(today.toString(), 'D', '-')+"\n";
	    s +=   "			</div>\n";
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n";
	    s +=   "	<tr>\n"; 
	    s +=   "		<td colspan=4 >\n";
	  	s +=   "			<table align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; 
	    s +=   "					<td width=50% valign=top align=center>\n";
	    s +=   "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "							<canvas id=\"customercount\" height=\"160px\"></canvas>\n";
	    s +=   "							<small class=\"form-text text-capitalize text-muted\">(data captured from Sales Module)</small>";
	    s +=   "						</div>\n";
	    s +=   "					</td>\n";
	    
	    s +=   "					<td width=50% valign=top align=center>\n";
	    s +=   "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "							<canvas id=\"avgcustomercount\" height=\"160px\"></canvas>\n";
	    s +=   "							<small class=\"form-text text-capitalize text-muted\">(data captured from Sales Module)</small>";
	    s +=   "						</div>\n";
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; 
	    s +=   "			</table>\n"; 
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n"; 
	    //********************** end 2nd row ***************************************//
	    //********************** 3rd row ***************************************//
	  	s +=   "	<tr>\n"; 
	    s +=   "		<td colspan=4 >\n";
	    s +=   "			<div class=\"hs-title\" >\n";
	    s +=   "				Basket Value<br>As of "+zabTools.dateDisplay(today.toString(), 'D', '-')+"\n";
	    s +=   "			</div>\n";
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n";
	    s +=   "	<tr>\n"; 
	    s +=   "		<td colspan=4>\n";
	  	s +=   "			<table align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; 
	    s +=   "					<td width=50% valign=top align=center>\n";
	    s +=   "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "							<canvas id=\"avgbasketvalue\" height=\"160px\"></canvas>\n";
	    s +=   "							<small class=\"form-text text-capitalize text-muted\">(data captured from Sales Module)</small>";
	    s +=   "						</div>\n";
	    s +=   "					</td>\n";
	    
	    s +=   "					<td width=50% valign=top>\n";
	    s +=   "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "							<canvas id=\"avgbasketvalu\" height=\"160px\"></canvas>\n";
	    s +=   "						</div>\n";
	    s +=   "					</td>\n";
	    
	    s +=   "				</tr>\n";
	    s +=   "			</table>\n"; 
	    
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n"; 
	    //********************** end 3rd row ***************************************//
	    s +=   "</td>\n"; // end of 80% td
	    s +=   "</tr>\n";
	    s +=   "</table>\n";
	    
	    //*****************GRAPH FOR LAST DAYS SALES ********************************//
		    
	    s +=   "	<script>\n";

	    label = "";
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+today.getYear()+" and xper="+today.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0)
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(2));
	   		  else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    
	    s +=   "var netsalesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [\n";
	    s +=   "			{\n";
	    s +=   "				label: '"+today.getMonth().toString()+", "+today.getYear()+"',\n";
// 	    s +=   "				backgroundColor: [\"red\", \"red\", \"yellow\", \"yellow\", \"blue\"],\n";
 	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";

	    label = "";
	    data = "";
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLastMonth.getYear()+" and xper="+firstDateOfLastMonth.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0)
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(2));
	   		  else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+"',\n";
 	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLast2Month.getYear()+" and xper="+firstDateOfLast2Month.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0)
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(2));
	   		  else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
 
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"',\n";
 	    s +=   "				backgroundColor: window.chartColors.grey,\n";
	    s +=   "				borderColor: window.chartColors.grey,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			plugins: {\n";
	    s +=   "				datalabels: {\n";
	    s +=   "					rotation:270,\n";
	    s +=   "					align:'top',\n";
	    s +=   "					anchor:'end',\n";
	    s +=   "					labels: {\n";
	    s +=   "						title: {\n";
	    s +=   "							color: 'black'\n";
	    s +=   "						}\n";
	    s +=   "					}\n";
	    s +=   "				}\n";
	    s +=   "			},\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Sales For "+today.getMonth().toString()+", "+today.getYear()+", "+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+", "+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Amount(Million) '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    //*****************END GRAPH FOR SALES ********************************//
	    
	    
	    //***************** GRAPH FOR AVERAGE SALES ********************************//
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));
	    
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+today.getYear()+" and xper="+today.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(new BigDecimal(noOfDaysCurMonth),2,RoundingMode.HALF_UP); 
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(3));
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
		    
	    
	    s +=   "var avgsalesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [\n";
	    s +=   "			{\n";
	    s +=   "				label: '"+today.getMonth().toString()+", "+today.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLastMonth.getYear()+" and xper="+firstDateOfLastMonth.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(new BigDecimal(noOfDaysLastMonth),2,RoundingMode.HALF_UP); 
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(3));
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLast2Month.getYear()+" and xper="+firstDateOfLast2Month.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(new BigDecimal(noOfDaysLast2Month),2,RoundingMode.HALF_UP); 
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(3));
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.grey,\n";
	    s +=   "				borderColor: window.chartColors.grey,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			plugins: {\n";
	    s +=   "				datalabels: {\n";
	    s +=   "					rotation:270,\n";
	    s +=   "					align:'top',\n";
	    s +=   "					anchor:'end',\n";
	    s +=   "					labels: {\n";
	    s +=   "						title: {\n";
	    s +=   "							color: 'black'\n";
	    s +=   "						}\n";
	    s +=   "					}\n";
	    s +=   "				}\n";
	    s +=   "			},\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Average Sales For "+today.getMonth().toString()+", "+today.getYear()+", "+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+", "+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Amount(Million) '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    //******************* END GRAPH FOR AVERAGE SALES ********************************//

	    //********************* GRAPH FOR CUSTOMER COUNT ********************************//
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+today.getYear()+" and xper="+today.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)==0)
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    
	    s +=   "var customercountonfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [\n";
	    s +=   "			{\n";
	    s +=   "				label: '"+today.getMonth().toString()+", "+today.getYear()+"',\n";
// 	    s +=   "				backgroundColor: [\"red\", \"red\", \"yellow\", \"yellow\", \"blue\"],\n";
 	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";

	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLastMonth.getYear()+" and xper="+firstDateOfLastMonth.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)== 0)
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+"',\n";
 	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLast2Month.getYear()+" and xper="+firstDateOfLast2Month.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)==0)
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}

	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"',\n";
 	    s +=   "				backgroundColor: window.chartColors.grey,\n";
	    s +=   "				borderColor: window.chartColors.grey,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			plugins: {\n";
	    s +=   "				datalabels: {\n";
	    s +=   "					rotation:270,\n";
	    s +=   "					align:'top',\n";
	    s +=   "					anchor:'end',\n";
	    s +=   "					labels: {\n";
	    s +=   "						title: {\n";
	    s +=   "							color: 'black'\n";
	    s +=   "						}\n";
	    s +=   "					}\n";
	    s +=   "				}\n";
	    s +=   "			},\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Customer Count For "+today.getMonth().toString()+", "+today.getYear()+", "+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+", "+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Number '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    //********************* END GRAPH FOR CUSTOMER COUNT ********************************//

	    
	    //********************* GRAPH FOR AVG CUSTOMER COUNT ********************************//
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));
	    
	    sql = "select xdiv,sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+today.getYear()+" and xper="+today.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(new BigDecimal(noOfDaysCurMonth),2,RoundingMode.HALF_UP); 
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
		    
	    
	    s +=   "var avgcustomercountconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [\n";
	    s +=   "			{\n";
	    s +=   "				label: '"+today.getMonth().toString()+", "+today.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLastMonth.getYear()+" and xper="+firstDateOfLastMonth.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(new BigDecimal(noOfDaysLastMonth),2,RoundingMode.HALF_UP); 
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLast2Month.getYear()+" and xper="+firstDateOfLast2Month.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(new BigDecimal(noOfDaysLast2Month),2,RoundingMode.HALF_UP); 
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.grey,\n";
	    s +=   "				borderColor: window.chartColors.grey,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			plugins: {\n";
	    s +=   "				datalabels: {\n";
	    s +=   "					rotation:270,\n";
	    s +=   "					align:'top',\n";
	    s +=   "					anchor:'end',\n";
	    s +=   "					labels: {\n";
	    s +=   "						title: {\n";
	    s +=   "							color: 'black'\n";
	    s +=   "						}\n";
	    s +=   "					}\n";
	    s +=   "				}\n";
	    s +=   "			},\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Average Customer Count For "+today.getMonth().toString()+", "+today.getYear()+", "+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+", "+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Number '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";


	    //********************* END GRAPH FOR AVG CUSTOMER COUNT ********************************//

	    //********************* GRAPH FOR AVG BASKET VALUE ********************************//
	    
	    label = "";
	    data = "";
	    BigDecimal invcount = new BigDecimal(0,new MathContext(0));
	    //BigDecimal totinvcount = new BigDecimal(0,new MathContext(0));
	    amount = new BigDecimal(0,new MathContext(0));
	    totamt = new BigDecimal(0,new MathContext(0));
	    
	    sql = "select xdiv,sum(xlineamt),sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+today.getYear()+" and xper="+today.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  invcount = zabTools.getBigDecimal(resultrows[i][2]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(invcount,2,RoundingMode.HALF_UP); 
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
		    
	    
	    s +=   "var avgbasketvalueconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [\n";
	    s +=   "			{\n";
	    s +=   "				label: '"+today.getMonth().toString()+", "+today.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    invcount = new BigDecimal(0,new MathContext(0));
	    amount = new BigDecimal(0,new MathContext(0));
	    totamt = new BigDecimal(0,new MathContext(0));

	    sql = "select xdiv,sum(xlineamt),sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLastMonth.getYear()+" and xper="+firstDateOfLastMonth.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  invcount = zabTools.getBigDecimal(resultrows[i][2]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(invcount,2,RoundingMode.HALF_UP); 
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    invcount = new BigDecimal(0,new MathContext(0));
	    amount = new BigDecimal(0,new MathContext(0));
	    totamt = new BigDecimal(0,new MathContext(0));

	    sql = "select xdiv,sum(xlineamt),sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLast2Month.getYear()+" and xper="+firstDateOfLast2Month.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  invcount = zabTools.getBigDecimal(resultrows[i][2]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(invcount,2,RoundingMode.HALF_UP); 
	   		  }else
	   			  amount = new BigDecimal(0);	
//	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
/*	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
*/
	   	  label = label.substring(0,label.length()-1);
	   	  label = "labels:["+label+"]";
	   	  data = data.substring(0,data.length()-1);

	   	  data = "data:["+data+"]";
	   	}
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.grey,\n";
	    s +=   "				borderColor: window.chartColors.grey,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			plugins: {\n";
	    s +=   "				datalabels: {\n";
	    s +=   "					rotation:270,\n";
	    s +=   "					align:'top',\n";
	    s +=   "					anchor:'end',\n";
	    s +=   "					labels: {\n";
	    s +=   "						title: {\n";
	    s +=   "							color: 'black'\n";
	    s +=   "						}\n";
	    s +=   "					}\n";
	    s +=   "				}\n";
	    s +=   "			},\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Average Basket Value For "+today.getMonth().toString()+", "+today.getYear()+", "+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+", "+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Number '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";


	    //********************* END GRAPH FOR AVG BASKET VALUE ********************************//


		s +=   "	window.onload = function() {\n";
	    s +=   "		var ctx = document.getElementById('netsales').getContext('2d');\n";
	    s +=   "		var chart = new Chart(ctx, netsalesconfig);\n";
	    s +=   "		var ctx1 = document.getElementById('avgsales').getContext('2d');\n";
	    s +=   "		var chart1 = new Chart(ctx1, avgsalesconfig);\n";
	    s +=   "		var ctx2 = document.getElementById('customercount').getContext('2d');\n";
	    s +=   "		var chart2 = new Chart(ctx2, customercountonfig);\n";
	    
	    s +=   "		var ctx3 = document.getElementById('avgcustomercount').getContext('2d');\n";
	    s +=   "		var chart3 = new Chart(ctx3, avgcustomercountconfig);\n";
	    
	    s +=   "		var ctx4 = document.getElementById('avgbasketvalue').getContext('2d');\n";
	    s +=   "		var chart4 = new Chart(ctx4, avgbasketvalueconfig);\n";
	    //s +=   "		var ctx5 = document.getElementById('lm2sales').getContext('2d');\n";
	    //s +=   "		var chart5 = new Chart(ctx5, lm2salesconfig);\n";
	    //s +=   "		var ctx6 = document.getElementById('lm2avgsales').getContext('2d');\n";
	    //s +=   "		var chart6 = new Chart(ctx6, lm2avgsalesconfig);\n";
	    
	    s +=   "	};\n";
	    
	    s +=   "							</script>\n";

	  return s;
  }
  
  
  
  
  
  
  
  
  
  //***** Test BY Mahbub ---****//
  
  
   public static String mahbub(zabInfo zabinfo){
	  
	  String s = "";
	  String caption = "Sales";
	  String name = "Sales";
	  
	  LocalDate today = LocalDate.of(2020,8,24);  //java.time.LocalDate.now();
	  LocalDate firstDateOfLastMonth = LocalDate.of(2020,7,1);
	  LocalDate lastDateOfLastMonth = LocalDate.of(2020,7,31);
	  long noOfDaysLastMonth = 31;
	  LocalDate firstDateOfLast2Month = LocalDate.of(2020,6,1);
	  LocalDate lastDateOfLast2Month = LocalDate.of(2020,6,30);
	  long noOfDaysLast2Month = 30;
/*
	  LocalDate today = java.time.LocalDate.now();
	  
	  LocalDate firstDateOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
	  LocalDate lastDateOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
	  long noOfDaysLastMonth = ChronoUnit.DAYS.between(firstDateOfLastMonth,lastDateOfLastMonth);

	  LocalDate firstDateOfLast2Month = today.minusMonths(2).with(TemporalAdjusters.firstDayOfMonth());
	  LocalDate lastDateOfLast2Month = today.minusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
	  long noOfDaysLast2Month = ChronoUnit.DAYS.between(firstDateOfLast2Month,lastDateOfLast2Month);
*/	  
	  LocalDate prvdate = today.minusDays(1);
//	  int period = today.getMonthValue();
	  int year = today.getYear();
//	  int yearOfLastMonth = year;
	  Month lastmonth = today.getMonth().minus(1);

	  LocalDate firstdate = today.with(TemporalAdjusters.firstDayOfMonth());
	  long noOfDaysCurMonth = ChronoUnit.DAYS.between(firstdate, today);
	  
	  BigDecimal amount = new BigDecimal("0");
	  BigDecimal totamt = new BigDecimal("0");
	  

	  //String today = java.time.LocalDate.now().toString();
	  //String prvdate = java.time.LocalDate.now().minusDays(1).toString();
	  //int per = java.time.LocalDate.now().getMonthValue();
//	  int year = java.time.LocalDate.now().getYear();
//	  System.err.println(java.time.LocalDate.now().getMonthValue());
//	  System.err.println(java.time.LocalDate.now().getMonth());

//	  int period = today.getMonthValue();
	  //int year = 2020;
	  //String today = "2020-08-24";
	  //String prvdate = "2020-08-23";
	  //String firstdate = year+"-08-01";   

	  String label = "";
	  //String [] period = {"1","2","3","4","5","6","7","8","9","10","11","12"};
	  String data = "";
	  String id = ""+zabinfo.ses.getAttribute("id");
	  
//	  String sp = "zabsp_Gunze_hsDashboard_Sales "+id+",'"+email+"','"+name+"',"+year;

	  //**** executing stored produre
	  //zabinfo.executeSP(sp);
   	  
   	  
   	  	s =   "<table id=\"hs-main-table\" align=center valign=top border=0 cellpadding=10 cellspacing=10 width=99% height=100%>\n";
	    s +=   "	<tr>\n";
	    s +=   "		<td colspan=2 align=left>\n";
//	    s +=   "			<div style=\"text-align:left;padding:10px;height:75px;background-color:skyblue;box-shadow:1px 1px 2px rgba(0, 0, 0, 0.4);font-size:20px;font-weight:bold;\">Test Data Mahbub<br>Date:"+zabTools.dateDisplay(today.toString(), 'D', '-')+"\n";
/*
	    s +=   "			<div style=\"text-align:left;padding:10px;height:75px;background-color:skyblue;box-shadow:1px 1px 2px rgba(0, 0, 0, 0.4);font-size:20px;font-weight:bold;\">\n";
	    s +=   "				<button type=\"button\" class=\"btn btn-info btn-lg\" data-toggle=\"modal\" data-target=\"#ccModal\">Customer Count</button>\n";
	    s +=   "				<button type=\"button\" class=\"btn btn-info btn-lg\" data-toggle=\"modal\" data-target=\"#abvModal\">Average Basket Value</button>\n";
	    s +=   "			</div>\n";
*/	    
	    s +=   "			<div class=\"hs-title\" >\n";
	    s +=   "				Test Data Mahbub<br>As of "+zabTools.dateDisplay(today.toString(), 'D', '-')+"\n";
	    s +=   "			</div>\n";
//		s +=   unimartMgnCusCount(zabinfo);
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n";

	    
	    //************************ 1st Row ************************************//

	    s +=   "	<tr>\n"; 
	    s +=   "		<td colspan=4>\n";
	  	s +=   "			<table align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; 
	  	
	    s +=   "					<td width=50% valign=top align=center>\n";
	    s +=   "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "							<canvas id=\"netsales\" height=160px></canvas>\n";
	    s +=   "							<small class=\"form-text text-capitalize text-muted\">(data captured from Sales Module)</small>";
	    s +=   "						</div>\n";
	    s +=   "					</td>\n";
	    
	    s +=   "					<td width=50% valign=top align=center>\n";
	    s +=   "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "							<canvas id=\"avgsales\" height=\"160px\"></canvas>\n";
	    s +=   "							<small class=\"form-text text-capitalize text-muted\">(data captured from Sales Module)</small>";
	    s +=   "						</div>\n";
	    s +=   "					</td>\n";
	    
	    s +=   "				</tr>\n"; 
	    s +=   "			</table>\n"; 
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n"; 
	    //************************ end 1st Row ************************************//

	    
	    //********************** 2nd row ***************************************//
	  	s +=   "	<tr>\n"; 
	    s +=   "		<td colspan=4 >\n";
	    s +=   "			<div class=\"hs-title\" >\n";
	    s +=   "				Customer Count<br>As of "+zabTools.dateDisplay(today.toString(), 'D', '-')+"\n";
	    s +=   "			</div>\n";
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n";
	    s +=   "	<tr>\n"; 
	    s +=   "		<td colspan=4 >\n";
	  	s +=   "			<table align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; 
	    s +=   "					<td width=50% valign=top align=center>\n";
	    s +=   "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "							<canvas id=\"customercount\" height=\"160px\"></canvas>\n";
	    s +=   "							<small class=\"form-text text-capitalize text-muted\">(data captured from Sales Module)</small>";
	    s +=   "						</div>\n";
	    s +=   "					</td>\n";
	    
	    s +=   "					<td width=50% valign=top align=center>\n";
	    s +=   "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "							<canvas id=\"avgcustomercount\" height=\"160px\"></canvas>\n";
	    s +=   "							<small class=\"form-text text-capitalize text-muted\">(data captured from Sales Module)</small>";
	    s +=   "						</div>\n";
	    s +=   "					</td>\n";
	    s +=   "				</tr>\n"; 
	    s +=   "			</table>\n"; 
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n"; 
	    //********************** end 2nd row ***************************************//
	    //********************** 3rd row ***************************************//
	  	s +=   "	<tr>\n"; 
	    s +=   "		<td colspan=4 >\n";
	    s +=   "			<div class=\"hs-title\" >\n";
	    s +=   "				Basket Value<br>As of "+zabTools.dateDisplay(today.toString(), 'D', '-')+"\n";
	    s +=   "			</div>\n";
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n";
	    s +=   "	<tr>\n"; 
	    s +=   "		<td colspan=4>\n";
	  	s +=   "			<table align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
	  	s +=   "				<tr>\n"; 
	    s +=   "					<td width=50% valign=top align=center>\n";
	    s +=   "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "							<canvas id=\"avgbasketvalue\" height=\"160px\"></canvas>\n";
	    s +=   "							<small class=\"form-text text-capitalize text-muted\">(data captured from Sales Module)</small>";
	    s +=   "						</div>\n";
	    s +=   "					</td>\n";
	    
	    s +=   "					<td width=50% valign=top>\n";
	    s +=   "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
	    s +=   "							<canvas id=\"avgbasketvalu\" height=\"160px\"></canvas>\n";
	    s +=   "						</div>\n";
	    s +=   "					</td>\n";
	    
	    s +=   "				</tr>\n";
	    s +=   "			</table>\n"; 
	    
	    s +=   "		</td>\n";
	    s +=   "	</tr>\n"; 
	    //********************** end 3rd row ***************************************//
	    s +=   "</td>\n"; // end of 80% td
	    s +=   "</tr>\n";
	    s +=   "</table>\n";
	    
	    //*****************GRAPH FOR LAST DAYS SALES ********************************//
		    
	    s +=   "	<script>\n";

	    label = "";
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+today.getYear()+" and xper="+today.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0)
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(2));
	   		  else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    
	    s +=   "var netsalesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [\n";
	    s +=   "			{\n";
	    s +=   "				label: '"+today.getMonth().toString()+", "+today.getYear()+"',\n";
// 	    s +=   "				backgroundColor: [\"red\", \"red\", \"yellow\", \"yellow\", \"blue\"],\n";
 	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";

	    label = "";
	    data = "";
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLastMonth.getYear()+" and xper="+firstDateOfLastMonth.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0)
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(2));
	   		  else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+"',\n";
 	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLast2Month.getYear()+" and xper="+firstDateOfLast2Month.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0)
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(2));
	   		  else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
 
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"',\n";
 	    s +=   "				backgroundColor: window.chartColors.grey,\n";
	    s +=   "				borderColor: window.chartColors.grey,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			plugins: {\n";
	    s +=   "				datalabels: {\n";
	    s +=   "					rotation:270,\n";
	    s +=   "					align:'top',\n";
	    s +=   "					anchor:'end',\n";
	    s +=   "					labels: {\n";
	    s +=   "						title: {\n";
	    s +=   "							color: 'black'\n";
	    s +=   "						}\n";
	    s +=   "					}\n";
	    s +=   "				}\n";
	    s +=   "			},\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Sales For "+today.getMonth().toString()+", "+today.getYear()+", "+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+", "+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Amount(Million) '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    //*****************END GRAPH FOR SALES ********************************//
	    
	    
	    //***************** GRAPH FOR AVERAGE SALES ********************************//
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));
	    
	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+today.getYear()+" and xper="+today.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(new BigDecimal(noOfDaysCurMonth),2,RoundingMode.HALF_UP); 
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(3));
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
		    
	    
	    s +=   "var avgsalesconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [\n";
	    s +=   "			{\n";
	    s +=   "				label: '"+today.getMonth().toString()+", "+today.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLastMonth.getYear()+" and xper="+firstDateOfLastMonth.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(new BigDecimal(noOfDaysLastMonth),2,RoundingMode.HALF_UP); 
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(3));
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xlineamt) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLast2Month.getYear()+" and xper="+firstDateOfLast2Month.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(new BigDecimal(noOfDaysLast2Month),2,RoundingMode.HALF_UP); 
	   			  amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(3));
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.grey,\n";
	    s +=   "				borderColor: window.chartColors.grey,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			plugins: {\n";
	    s +=   "				datalabels: {\n";
	    s +=   "					rotation:270,\n";
	    s +=   "					align:'top',\n";
	    s +=   "					anchor:'end',\n";
	    s +=   "					labels: {\n";
	    s +=   "						title: {\n";
	    s +=   "							color: 'black'\n";
	    s +=   "						}\n";
	    s +=   "					}\n";
	    s +=   "				}\n";
	    s +=   "			},\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Average Sales For "+today.getMonth().toString()+", "+today.getYear()+", "+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+", "+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Amount(Million) '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    //******************* END GRAPH FOR AVERAGE SALES ********************************//

	    //********************* GRAPH FOR CUSTOMER COUNT ********************************//
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+today.getYear()+" and xper="+today.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)==0)
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    
	    s +=   "var customercountonfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [\n";
	    s +=   "			{\n";
	    s +=   "				label: '"+today.getMonth().toString()+", "+today.getYear()+"',\n";
// 	    s +=   "				backgroundColor: [\"red\", \"red\", \"yellow\", \"yellow\", \"blue\"],\n";
 	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";

	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLastMonth.getYear()+" and xper="+firstDateOfLastMonth.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)== 0)
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+"',\n";
 	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLast2Month.getYear()+" and xper="+firstDateOfLast2Month.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)==0)
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}

	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"',\n";
 	    s +=   "				backgroundColor: window.chartColors.grey,\n";
	    s +=   "				borderColor: window.chartColors.grey,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			plugins: {\n";
	    s +=   "				datalabels: {\n";
	    s +=   "					rotation:270,\n";
	    s +=   "					align:'top',\n";
	    s +=   "					anchor:'end',\n";
	    s +=   "					labels: {\n";
	    s +=   "						title: {\n";
	    s +=   "							color: 'black'\n";
	    s +=   "						}\n";
	    s +=   "					}\n";
	    s +=   "				}\n";
	    s +=   "			},\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Customer Count For "+today.getMonth().toString()+", "+today.getYear()+", "+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+", "+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Number '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";

	    //********************* END GRAPH FOR CUSTOMER COUNT ********************************//

	    
	    //********************* GRAPH FOR AVG CUSTOMER COUNT ********************************//
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));
	    
	    sql = "select xdiv,sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+today.getYear()+" and xper="+today.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(new BigDecimal(noOfDaysCurMonth),2,RoundingMode.HALF_UP); 
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
		    
	    
	    s +=   "var avgcustomercountconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [\n";
	    s +=   "			{\n";
	    s +=   "				label: '"+today.getMonth().toString()+", "+today.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLastMonth.getYear()+" and xper="+firstDateOfLastMonth.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(new BigDecimal(noOfDaysLastMonth),2,RoundingMode.HALF_UP); 
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    amount = new BigDecimal(0,new MathContext(2));
	    totamt = new BigDecimal(0,new MathContext(2));

	    sql = "select xdiv,sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLast2Month.getYear()+" and xper="+firstDateOfLast2Month.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(new BigDecimal(noOfDaysLast2Month),2,RoundingMode.HALF_UP); 
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.grey,\n";
	    s +=   "				borderColor: window.chartColors.grey,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			plugins: {\n";
	    s +=   "				datalabels: {\n";
	    s +=   "					rotation:270,\n";
	    s +=   "					align:'top',\n";
	    s +=   "					anchor:'end',\n";
	    s +=   "					labels: {\n";
	    s +=   "						title: {\n";
	    s +=   "							color: 'black'\n";
	    s +=   "						}\n";
	    s +=   "					}\n";
	    s +=   "				}\n";
	    s +=   "			},\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Average Customer Count For "+today.getMonth().toString()+", "+today.getYear()+", "+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+", "+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Number '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";


	    //********************* END GRAPH FOR AVG CUSTOMER COUNT ********************************//

	    //********************* GRAPH FOR AVG BASKET VALUE ********************************//
	    
	    label = "";
	    data = "";
	    BigDecimal invcount = new BigDecimal(0,new MathContext(0));
	    //BigDecimal totinvcount = new BigDecimal(0,new MathContext(0));
	    amount = new BigDecimal(0,new MathContext(0));
	    totamt = new BigDecimal(0,new MathContext(0));
	    
	    sql = "select xdiv,sum(xlineamt),sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+today.getYear()+" and xper="+today.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  invcount = zabTools.getBigDecimal(resultrows[i][2]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(invcount,2,RoundingMode.HALF_UP); 
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
		    
	    
	    s +=   "var avgbasketvalueconfig = {\n";
	    s +=   "		type: 'bar',\n";
	    s +=   "		data: {\n";
	    s +=   "			"+label+",\n";
	    s +=   "			datasets: [\n";
	    s +=   "			{\n";
	    s +=   "				label: '"+today.getMonth().toString()+", "+today.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.blue,\n";
	    s +=   "				borderColor: window.chartColors.blue,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    invcount = new BigDecimal(0,new MathContext(0));
	    amount = new BigDecimal(0,new MathContext(0));
	    totamt = new BigDecimal(0,new MathContext(0));

	    sql = "select xdiv,sum(xlineamt),sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLastMonth.getYear()+" and xper="+firstDateOfLastMonth.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  invcount = zabTools.getBigDecimal(resultrows[i][2]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(invcount,2,RoundingMode.HALF_UP); 
	   		  }else
	   			  amount = new BigDecimal(0);	
	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
	   	  data = "data:["+data+"]";
	   	}
	    
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.red,\n";
	    s +=   "				borderColor: window.chartColors.red,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    
	    label = "";
	    data = "";
	    invcount = new BigDecimal(0,new MathContext(0));
	    amount = new BigDecimal(0,new MathContext(0));
	    totamt = new BigDecimal(0,new MathContext(0));

	    sql = "select xdiv,sum(xlineamt),sum(xinvcount) from oprptsalessum";
	    sql += " where zid='"+id+"'";
	    sql += " and xyear="+firstDateOfLast2Month.getYear()+" and xper="+firstDateOfLast2Month.getMonthValue();
	    sql += " group by xdiv";
	    sql += " order by xdiv";
	   	resultrows = zabinfo.getSqlRows(sql);
	   	if(resultrows==null){
		   	label = "labels:[]";
	   		data = "data: []";
	   	}else{
	   	  for(int i=0;i<resultrows.length;i++) {	
	   		  label += "'"+resultrows[i][0]+"',";
	   		  amount = zabTools.getBigDecimal(resultrows[i][1]);
	   		  invcount = zabTools.getBigDecimal(resultrows[i][2]);
	   		  if(amount.compareTo(BigDecimal.ZERO)>0) {
	   			  amount = amount.divide(invcount,2,RoundingMode.HALF_UP); 
	   		  }else
	   			  amount = new BigDecimal(0);	
//	   		  totamt = totamt.add(amount);
	   		  data += amount+",";
	   	  }
/*	   	  
	   	  label = label+"'Total'";
	   	  label = "labels:["+label+"]";
	   	  data = data+totamt.toString();
*/
	   	  label = label.substring(0,label.length()-1);
	   	  label = "labels:["+label+"]";
	   	  data = data.substring(0,data.length()-1);

	   	  data = "data:["+data+"]";
	   	}
	    s +=   "			{\n";
	    s +=   "				label: '"+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"',\n";
	    s +=   "				backgroundColor: window.chartColors.grey,\n";
	    s +=   "				borderColor: window.chartColors.grey,\n";
	    s +=   "				"+data+",\n";
	    s +=   "				fill: false,\n";
	    s +=   "			},\n";
	    s +=   "		]\n";
	    s +=   "		},\n";
	    s +=   "		options: {\n";
	    s +=   "			plugins: {\n";
	    s +=   "				datalabels: {\n";
	    s +=   "					rotation:270,\n";
	    s +=   "					align:'top',\n";
	    s +=   "					anchor:'end',\n";
	    s +=   "					labels: {\n";
	    s +=   "						title: {\n";
	    s +=   "							color: 'black'\n";
	    s +=   "						}\n";
	    s +=   "					}\n";
	    s +=   "				}\n";
	    s +=   "			},\n";
	    s +=   "			responsive: true,\n";
	    s +=   "			title: {\n";
	    s +=   "				display: true,\n";
	    s +=   "				fontSize: 18,\n";
	    s +=   "				text: 'Average Basket Value For "+today.getMonth().toString()+", "+today.getYear()+", "+firstDateOfLastMonth.getMonth().toString()+", "+firstDateOfLastMonth.getYear()+", "+firstDateOfLast2Month.getMonth().toString()+", "+firstDateOfLast2Month.getYear()+"'\n";
	    s +=   "			},\n";
	    s +=   "			tooltips: {\n";
	    s +=   "				mode: 'index',\n";
	    s +=   "				intersect: false,\n";
	    s +=   "    			callbacks: {\n";
	    s +=   "					label: function(tooltipItem, data){\n";
	    s +=   "        				return addCommas(tooltipItem.yLabel);\n";
	    s +=   "    				}\n";
	    s +=   "    			}\n";
	    s +=   "			},\n";
	    s +=   "			hover: {\n";
	    s +=   "				mode: 'nearest',\n";
	    s +=   "				intersect: true\n";
	    s +=   "			},\n";
	    s +=   "			scales: {\n";
	    s +=   "				xAxes: [{\n";
	    s +=   "					display: true,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: false\n";
	    //s +=   "						labelString: 'Month'\n";
	    s +=   "					}\n";
	    s +=   "				}],\n";
	    s +=   "				yAxes: [{\n";
	    s +=   "					display: true,\n";
	    //s +=   "					lineWidth: 12.5,\n";
	    s +=   "					scaleLabel: {\n";
	    s +=   "						display: true,\n";
	    s +=   "						labelString: 'Number '\n";
  		s +=   "					},\n";
	    s +=   "					ticks: {\n";
	    s +=   "    					beginAtZero:true,\n";
	    s +=   "    					callback: function(value) {\n";
	    s +=   "        					return addCommas(value);\n";
	    s +=   "    					}\n";
	    s +=   "					}\n";
	    s +=   "				}]\n";
	    s +=   "			}\n";
	    s +=   "		}\n";
	    s +=   "	};\n";


	    //********************* END GRAPH FOR AVG BASKET VALUE ********************************//


		s +=   "	window.onload = function() {\n";
	    s +=   "		var ctx = document.getElementById('netsales').getContext('2d');\n";
	    s +=   "		var chart = new Chart(ctx, netsalesconfig);\n";
	    s +=   "		var ctx1 = document.getElementById('avgsales').getContext('2d');\n";
	    s +=   "		var chart1 = new Chart(ctx1, avgsalesconfig);\n";
	    s +=   "		var ctx2 = document.getElementById('customercount').getContext('2d');\n";
	    s +=   "		var chart2 = new Chart(ctx2, customercountonfig);\n";
	    
	    s +=   "		var ctx3 = document.getElementById('avgcustomercount').getContext('2d');\n";
	    s +=   "		var chart3 = new Chart(ctx3, avgcustomercountconfig);\n";
	    
	    s +=   "		var ctx4 = document.getElementById('avgbasketvalue').getContext('2d');\n";
	    s +=   "		var chart4 = new Chart(ctx4, avgbasketvalueconfig);\n";
	    //s +=   "		var ctx5 = document.getElementById('lm2sales').getContext('2d');\n";
	    //s +=   "		var chart5 = new Chart(ctx5, lm2salesconfig);\n";
	    //s +=   "		var ctx6 = document.getElementById('lm2avgsales').getContext('2d');\n";
	    //s +=   "		var chart6 = new Chart(ctx6, lm2avgsalesconfig);\n";
	    
	    s +=   "	};\n";
	    
	    s +=   "							</script>\n";

	  return s;
  }
  
  
 
}

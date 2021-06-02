package zaberp.zab;

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
	static String[] result = null;
	static String[][] resultrows = null;
	static String[] sa = null;

	public zabHomeScreen() {
	}

	public static String specialPage(zabInfo zabinfo) {
		String homescreen = (String) zabinfo.ses.getAttribute("homescreen");

		if (homescreen.equals("unimartmanagement")) {
			return unimartmanagement(zabinfo);
		}
		return "";
	}

	public static String unimartmanagement(zabInfo zabinfo) {

		String s = "";

		LocalDate today = java.time.LocalDate.now();
		LocalDate previousday = today.minusDays(1);
		LocalDate firstDateOfLastMonth = previousday.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDateOfLastMonth = previousday.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
		long noOfDaysLastMonth = ChronoUnit.DAYS.between(firstDateOfLastMonth, lastDateOfLastMonth);
		LocalDate firstDateOfLast2Month = previousday.minusMonths(2).with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDateOfLast2Month = previousday.minusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
		long noOfDaysLast2Month = ChronoUnit.DAYS.between(firstDateOfLast2Month, lastDateOfLast2Month);

		LocalDate firstdate = previousday.with(TemporalAdjusters.firstDayOfMonth());
		long noOfDaysCurMonth = ChronoUnit.DAYS.between(firstdate, previousday);

		BigDecimal amount = new BigDecimal("0");
		BigDecimal totamt = new BigDecimal("0");

		String label = "";
		String data = "";
		String id = "" + zabinfo.ses.getAttribute("id");

		s = "<table id=\"hs-main-table\" align=center valign=top border=0 cellpadding=10 cellspacing=10 width=99% height=100%>\n";
		s += "	<tr>\n";
		s += "		<td colspan=2 align=left>\n";

		s += "			<div class=\"hs-title\" style=\"text-align:center; height:60px; background-color:skyblue; box-shadow:1px 1px 2px 2px rgba(0, 0, 0, 0.4); font:Helvetica Neue, Helvetica, Arial, sans-serif; font-size:20px; color:white;\" >\n";
		s += "				Net Sales<br>As of " + zabTools.dateDisplay(previousday.toString(), 'D', '-') + "\n";
		s += "			</div>\n";
		s += "		</td>\n";
		s += "	</tr>\n";

		// ************************ 1st Row ************************************//
		s += "	<tr>\n";
		s += "		<td colspan=4>\n";
		s += "			<table align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
		s += "				<tr>\n";

		s += "					<td width=50% valign=top>\n";
		s += "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
		s += "							<canvas id=\"netsales\" height=160px></canvas>\n";
		s += "						</div>\n";
		s += "					</td>\n";

		s += "					<td width=50% valign=top>\n";
		s += "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
		s += "							<canvas id=\"avgsales\" height=\"160px\"></canvas>\n";
		s += "						</div>\n";
		s += "					</td>\n";

		s += "				</tr>\n";
		s += "			</table>\n";
		s += "		</td>\n";
		s += "	</tr>\n";
		// ************************ end 1st Row ************************************//

		// ********************** 2nd row ***************************************//
		s += "	<tr>\n";
		s += "		<td colspan=4 >\n";
		s += "			<div class=\"hs-title\" style=\"text-align:center; height:60px; background-color:skyblue; box-shadow:1px 1px 2px 2px rgba(0, 0, 0, 0.4); font:Helvetica Neue, Helvetica, Arial, sans-serif; font-size:20px; color:white;\" >\n";
		s += "				Customer Count<br>As of " + zabTools.dateDisplay(previousday.toString(), 'D', '-') + "\n";
		s += "			</div>\n";
		s += "		</td>\n";
		s += "	</tr>\n";
		s += "	<tr>\n";
		s += "		<td colspan=4 class=\"hs-info-td\">\n";
		s += "			<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
		s += "				<tr>\n";
		s += "					<td width=50% valign=top>\n";
		s += "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
		s += "							<canvas id=\"customercount\" height=\"160px\"></canvas>\n";
		s += "						</div>\n";
		s += "					</td>\n";

		s += "					<td width=50% valign=top>\n";
		s += "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
		s += "							<canvas id=\"avgcustomercount\" height=\"160px\"></canvas>\n";
		s += "						</div>\n";
		s += "					</td>\n";
		s += "				</tr>\n";
		s += "			</table>\n";
		s += "		</td>\n";
		s += "	</tr>\n";
		// ********************** end 2nd row ***************************************//
		// ********************** 3rd row ***************************************//
		s += "	<tr>\n";
		s += "		<td colspan=4 >\n";
		s += "			<div class=\"hs-title\" style=\"text-align:center; height:60px; background-color:skyblue; box-shadow:1px 1px 2px 2px rgba(0, 0, 0, 0.4); font:Helvetica Neue, Helvetica, Arial, sans-serif; font-size:20px; color:white;\" >\n";
		s += "				Basket Value<br>As of " + zabTools.dateDisplay(previousday.toString(), 'D', '-') + "\n";
		s += "			</div>\n";
		s += "		</td>\n";
		s += "	</tr>\n";
		s += "	<tr>\n";
		s += "		<td colspan=4 class=\"hs-info-td\">\n";
		s += "			<table id=\"table-hs-right\" align=left valign=top border=0 cellpadding=0 cellspacing=0 width=100%>\n";
		s += "				<tr>\n";
		s += "					<td width=50% valign=top>\n";
		s += "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
		s += "							<canvas id=\"avgbasketvalue\" height=\"160px\"></canvas>\n";
		s += "						</div>\n";
		s += "					</td>\n";

		s += "					<td width=50% valign=top>\n";
		s += "						<div style=\"margin-right:5px;background-color:#fff;\">\n";
		s += "							<canvas id=\"avgbasketvalu\" height=\"160px\"></canvas>\n";
		s += "						</div>\n";
		s += "					</td>\n";

		s += "				</tr>\n";
		s += "			</table>\n";

		s += "		</td>\n";
		s += "	</tr>\n";
		// ********************** end 3rd row ***************************************//
		s += "</td>\n"; // end of 80% td
		s += "</tr>\n";
		s += "</table>\n";

		// *****************GRAPH FOR LAST DAYS SALES ********************************//
		s += "	<script>\n";

		label = "";
		sql = "select xdiv,sum(xlineamt) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + previousday.getYear() + " and xper=" + previousday.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				if (amount.compareTo(BigDecimal.ZERO) > 0) {
//					amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(2));
					amount = amount.divide(new BigDecimal("1000000")).setScale(2, RoundingMode.HALF_UP);
				} else {
					amount = new BigDecimal(0);
				}
				totamt = totamt.add(amount);
				data += amount + ",";
			}
			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}

		s += "var netsalesconfig = {\n";
		s += "		type: 'bar',\n";
		s += "		data: {\n";
		s += "			" + label + ",\n";
		s += "			datasets: [\n";
		s += "			{\n";
		s += "				label: '" + previousday.getMonth().toString() + ", " + previousday.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.blue,\n";
		s += "				borderColor: window.chartColors.blue,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";

		label = "";
		data = "";
		amount = new BigDecimal(0, new MathContext(2));
		totamt = new BigDecimal(0, new MathContext(2));
		sql = "select xdiv,sum(xlineamt) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + firstDateOfLastMonth.getYear() + " and xper=" + firstDateOfLastMonth.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				if (amount.compareTo(BigDecimal.ZERO) > 0) {
//					amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(2));
					amount = amount.divide(new BigDecimal("1000000")).setScale(2, RoundingMode.HALF_UP);
				} else {
					amount = new BigDecimal(0);
				}
				totamt = totamt.add(amount);
				data += amount + ",";
			}
			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}

		s += "			{\n";
		s += "				label: '" + firstDateOfLastMonth.getMonth().toString() + ", "
				+ firstDateOfLastMonth.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.red,\n";
		s += "				borderColor: window.chartColors.red,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";

		label = "";
		data = "";
		amount = new BigDecimal(0, new MathContext(2));
		totamt = new BigDecimal(0, new MathContext(2));
		sql = "select xdiv,sum(xlineamt) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + firstDateOfLast2Month.getYear() + " and xper=" + firstDateOfLast2Month.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				if (amount.compareTo(BigDecimal.ZERO) > 0)
//					amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(2));
					amount = amount.divide(new BigDecimal("1000000")).setScale(2, RoundingMode.HALF_UP);
				else
					amount = new BigDecimal(0);
				totamt = totamt.add(amount);
				data += amount + ",";
			}
			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}

		s += "			{\n";
		s += "				label: '" + firstDateOfLast2Month.getMonth().toString() + ", "
				+ firstDateOfLast2Month.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.grey,\n";
		s += "				borderColor: window.chartColors.grey,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";
		s += "		]\n";
		s += "		},\n";
		s += "		options: {\n";
		s += "			plugins: {\n";
		s += "				datalabels: {\n";
		s += "					rotation:270,\n";
		s += "					align:'top',\n";
		s += "					anchor:'end',\n";
		s += "					labels: {\n";
		s += "						title: {\n";
		s += "							color: 'black'\n";
		s += "						}\n";
		s += "					}\n";
		s += "				}\n";
		s += "			},\n";
		s += "			responsive: true,\n";
		s += "			title: {\n";
		s += "				display: true,\n";
		s += "				fontSize: 18,\n";
		s += "				text: 'Sales For " + previousday.getMonth().toString() + ", " + previousday.getYear() + ", "
				+ firstDateOfLastMonth.getMonth().toString() + ", " + firstDateOfLastMonth.getYear() + ", "
				+ firstDateOfLast2Month.getMonth().toString() + ", " + firstDateOfLast2Month.getYear() + "'\n";
		s += "			},\n";
		s += "			tooltips: {\n";
		s += "				mode: 'index',\n";
		s += "				intersect: false,\n";
		s += "    			callbacks: {\n";
		s += "					label: function(tooltipItem, data){\n";
		s += "        				return addCommas(tooltipItem.yLabel);\n";
		s += "    				}\n";
		s += "    			}\n";
		s += "			},\n";
		s += "			hover: {\n";
		s += "				mode: 'nearest',\n";
		s += "				intersect: true\n";
		s += "			},\n";
		s += "			scales: {\n";
		s += "				xAxes: [{\n";
		s += "					display: true,\n";
		s += "					scaleLabel: {\n";
		s += "						display: false\n";
		s += "					}\n";
		s += "				}],\n";
		s += "				yAxes: [{\n";
		s += "					display: true,\n";
		s += "					scaleLabel: {\n";
		s += "						display: true,\n";
		s += "						labelString: 'Amount(Million) '\n";
		s += "					},\n";
		s += "					ticks: {\n";
		s += "    					beginAtZero:true,\n";
		s += "    					callback: function(value) {\n";
		s += "        					return addCommas(value);\n";
		s += "    					}\n";
		s += "					}\n";
		s += "				}]\n";
		s += "			}\n";
		s += "		}\n";
		s += "	};\n";
		// *****************END GRAPH FOR SALES ********************************//

		// ***************** GRAPH FOR AVERAGE SALES ********************************//
		label = "";
		data = "";
		amount = new BigDecimal(0, new MathContext(2));
		totamt = new BigDecimal(0, new MathContext(2));

		sql = "select xdiv,sum(xlineamt) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + previousday.getYear() + " and xper=" + previousday.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				if (amount.compareTo(BigDecimal.ZERO) > 0) {
					if (noOfDaysCurMonth > 0) {
						amount = amount.divide(new BigDecimal(noOfDaysCurMonth), 2, RoundingMode.HALF_UP);
					}
//					amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(3));
					amount = amount.divide(new BigDecimal("1000000")).setScale(2, RoundingMode.HALF_UP);
				} else
					amount = new BigDecimal(0);
				totamt = totamt.add(amount);
				data += amount + ",";
			}

			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}

		s += "var avgsalesconfig = {\n";
		s += "		type: 'bar',\n";
		s += "		data: {\n";
		s += "			" + label + ",\n";
		s += "			datasets: [\n";
		s += "			{\n";
		s += "				label: '" + previousday.getMonth().toString() + ", " + previousday.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.blue,\n";
		s += "				borderColor: window.chartColors.blue,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";

		label = "";
		data = "";
		amount = new BigDecimal(0, new MathContext(2));
		totamt = new BigDecimal(0, new MathContext(2));

		sql = "select xdiv,sum(xlineamt) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + firstDateOfLastMonth.getYear() + " and xper=" + firstDateOfLastMonth.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				if (amount.compareTo(BigDecimal.ZERO) > 0) {
					if (noOfDaysLastMonth > 0) {
						amount = amount.divide(new BigDecimal(noOfDaysLastMonth), 2, RoundingMode.HALF_UP);
					}
//					amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(3));
					amount = amount.divide(new BigDecimal("1000000")).setScale(2, RoundingMode.HALF_UP);
				} else
					amount = new BigDecimal(0);
				totamt = totamt.add(amount);
				data += amount + ",";
			}

			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}

		s += "			{\n";
		s += "				label: '" + firstDateOfLastMonth.getMonth().toString() + ", "
				+ firstDateOfLastMonth.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.red,\n";
		s += "				borderColor: window.chartColors.red,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";

		label = "";
		data = "";
		amount = new BigDecimal(0, new MathContext(2));
		totamt = new BigDecimal(0, new MathContext(2));

		sql = "select xdiv,sum(xlineamt) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + firstDateOfLast2Month.getYear() + " and xper=" + firstDateOfLast2Month.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				if (amount.compareTo(BigDecimal.ZERO) > 0) {
					if (noOfDaysLast2Month > 0) {
						amount = amount.divide(new BigDecimal(noOfDaysLast2Month), 2, RoundingMode.HALF_UP);
					}
//					amount = amount.divide(new BigDecimal("1000000")).round(new MathContext(3));
					amount = amount.divide(new BigDecimal("1000000")).setScale(2, RoundingMode.HALF_UP);
				} else
					amount = new BigDecimal(0);
				totamt = totamt.add(amount);
				data += amount + ",";
			}

			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}
		s += "			{\n";
		s += "				label: '" + firstDateOfLast2Month.getMonth().toString() + ", "
				+ firstDateOfLast2Month.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.grey,\n";
		s += "				borderColor: window.chartColors.grey,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";
		s += "		]\n";
		s += "		},\n";
		s += "		options: {\n";
		s += "			plugins: {\n";
		s += "				datalabels: {\n";
		s += "					rotation:270,\n";
		s += "					align:'top',\n";
		s += "					anchor:'end',\n";
		s += "					labels: {\n";
		s += "						title: {\n";
		s += "							color: 'black'\n";
		s += "						}\n";
		s += "					}\n";
		s += "				}\n";
		s += "			},\n";
		s += "			responsive: true,\n";
		s += "			title: {\n";
		s += "				display: true,\n";
		s += "				fontSize: 18,\n";
		s += "				text: 'Average Sales For " + previousday.getMonth().toString() + ", "
				+ previousday.getYear() + ", " + firstDateOfLastMonth.getMonth().toString() + ", "
				+ firstDateOfLastMonth.getYear() + ", " + firstDateOfLast2Month.getMonth().toString() + ", "
				+ firstDateOfLast2Month.getYear() + "'\n";
		s += "			},\n";
		s += "			tooltips: {\n";
		s += "				mode: 'index',\n";
		s += "				intersect: false,\n";
		s += "    			callbacks: {\n";
		s += "					label: function(tooltipItem, data){\n";
		s += "        				return addCommas(tooltipItem.yLabel);\n";
		s += "    				}\n";
		s += "    			}\n";
		s += "			},\n";
		s += "			hover: {\n";
		s += "				mode: 'nearest',\n";
		s += "				intersect: true\n";
		s += "			},\n";
		s += "			scales: {\n";
		s += "				xAxes: [{\n";
		s += "					display: true,\n";
		s += "					scaleLabel: {\n";
		s += "						display: false\n";
		s += "					}\n";
		s += "				}],\n";
		s += "				yAxes: [{\n";
		s += "					display: true,\n";
		s += "					scaleLabel: {\n";
		s += "						display: true,\n";
		s += "						labelString: 'Amount(Million) '\n";
		s += "					},\n";
		s += "					ticks: {\n";
		s += "    					beginAtZero:true,\n";
		s += "    					callback: function(value) {\n";
		s += "        					return addCommas(value);\n";
		s += "    					}\n";
		s += "					}\n";
		s += "				}]\n";
		s += "			}\n";
		s += "		}\n";
		s += "	};\n";
		// ******************* END GRAPH FOR AVERAGE SALES
		// ********************************//

		// ********************* GRAPH FOR CUSTOMER COUNT
		// ********************************//
		label = "";
		data = "";
		amount = new BigDecimal(0, new MathContext(2));
		totamt = new BigDecimal(0, new MathContext(2));

		sql = "select xdiv,sum(xinvcount) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + previousday.getYear() + " and xper=" + previousday.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				if (amount.compareTo(BigDecimal.ZERO) == 0)
					amount = new BigDecimal(0);
				totamt = totamt.add(amount);
				data += amount + ",";
			}
			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}

		s += "var customercountonfig = {\n";
		s += "		type: 'bar',\n";
		s += "		data: {\n";
		s += "			" + label + ",\n";
		s += "			datasets: [\n";
		s += "			{\n";
		s += "				label: '" + previousday.getMonth().toString() + ", " + previousday.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.blue,\n";
		s += "				borderColor: window.chartColors.blue,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";

		label = "";
		data = "";
		amount = new BigDecimal(0, new MathContext(2));
		totamt = new BigDecimal(0, new MathContext(2));

		sql = "select xdiv,sum(xinvcount) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + firstDateOfLastMonth.getYear() + " and xper=" + firstDateOfLastMonth.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				if (amount.compareTo(BigDecimal.ZERO) == 0)
					amount = new BigDecimal(0);
				totamt = totamt.add(amount);
				data += amount + ",";
			}
			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}
		s += "			{\n";
		s += "				label: '" + firstDateOfLastMonth.getMonth().toString() + ", "
				+ firstDateOfLastMonth.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.red,\n";
		s += "				borderColor: window.chartColors.red,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";

		label = "";
		data = "";
		amount = new BigDecimal(0, new MathContext(2));
		totamt = new BigDecimal(0, new MathContext(2));

		sql = "select xdiv,sum(xinvcount) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + firstDateOfLast2Month.getYear() + " and xper=" + firstDateOfLast2Month.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				if (amount.compareTo(BigDecimal.ZERO) == 0)
					amount = new BigDecimal(0);
				totamt = totamt.add(amount);
				data += amount + ",";
			}
			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}

		s += "			{\n";
		s += "				label: '" + firstDateOfLast2Month.getMonth().toString() + ", "
				+ firstDateOfLast2Month.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.grey,\n";
		s += "				borderColor: window.chartColors.grey,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";
		s += "		]\n";
		s += "		},\n";
		s += "		options: {\n";
		s += "			plugins: {\n";
		s += "				datalabels: {\n";
		s += "					rotation:270,\n";
		s += "					align:'top',\n";
		s += "					anchor:'end',\n";
		s += "					labels: {\n";
		s += "						title: {\n";
		s += "							color: 'black'\n";
		s += "						}\n";
		s += "					}\n";
		s += "				}\n";
		s += "			},\n";
		s += "			responsive: true,\n";
		s += "			title: {\n";
		s += "				display: true,\n";
		s += "				fontSize: 18,\n";
		s += "				text: 'Customer Count For " + previousday.getMonth().toString() + ", "
				+ previousday.getYear() + ", " + firstDateOfLastMonth.getMonth().toString() + ", "
				+ firstDateOfLastMonth.getYear() + ", " + firstDateOfLast2Month.getMonth().toString() + ", "
				+ firstDateOfLast2Month.getYear() + "'\n";
		s += "			},\n";
		s += "			tooltips: {\n";
		s += "				mode: 'index',\n";
		s += "				intersect: false,\n";
		s += "    			callbacks: {\n";
		s += "					label: function(tooltipItem, data){\n";
		s += "        				return addCommas(tooltipItem.yLabel);\n";
		s += "    				}\n";
		s += "    			}\n";
		s += "			},\n";
		s += "			hover: {\n";
		s += "				mode: 'nearest',\n";
		s += "				intersect: true\n";
		s += "			},\n";
		s += "			scales: {\n";
		s += "				xAxes: [{\n";
		s += "					display: true,\n";
		s += "					scaleLabel: {\n";
		s += "						display: false\n";
		s += "					}\n";
		s += "				}],\n";
		s += "				yAxes: [{\n";
		s += "					display: true,\n";
		s += "					scaleLabel: {\n";
		s += "						display: true,\n";
		s += "						labelString: 'Number '\n";
		s += "					},\n";
		s += "					ticks: {\n";
		s += "    					beginAtZero:true,\n";
		s += "    					callback: function(value) {\n";
		s += "        					return addCommas(value);\n";
		s += "    					}\n";
		s += "					}\n";
		s += "				}]\n";
		s += "			}\n";
		s += "		}\n";
		s += "	};\n";
		// ********************* END GRAPH FOR CUSTOMER COUNT
		// ********************************//

		// ********************* GRAPH FOR AVG CUSTOMER COUNT
		// ********************************//
		label = "";
		data = "";
		amount = new BigDecimal(0, new MathContext(2));
		totamt = new BigDecimal(0, new MathContext(2));

		sql = "select xdiv,sum(xinvcount) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + previousday.getYear() + " and xper=" + previousday.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				if (amount.compareTo(BigDecimal.ZERO) > 0) {
					if (noOfDaysCurMonth > 0) {
						amount = amount.divide(new BigDecimal(noOfDaysCurMonth), 2, RoundingMode.HALF_UP);
					}
				} else
					amount = new BigDecimal(0);
				totamt = totamt.add(amount);
				data += amount + ",";
			}

			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}

		s += "var avgcustomercountconfig = {\n";
		s += "		type: 'bar',\n";
		s += "		data: {\n";
		s += "			" + label + ",\n";
		s += "			datasets: [\n";
		s += "			{\n";
		s += "				label: '" + previousday.getMonth().toString() + ", " + previousday.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.blue,\n";
		s += "				borderColor: window.chartColors.blue,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";

		label = "";
		data = "";
		amount = new BigDecimal(0, new MathContext(2));
		totamt = new BigDecimal(0, new MathContext(2));

		sql = "select xdiv,sum(xinvcount) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + firstDateOfLastMonth.getYear() + " and xper=" + firstDateOfLastMonth.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				if (amount.compareTo(BigDecimal.ZERO) > 0) {
					if (noOfDaysLastMonth > 0) {
						amount = amount.divide(new BigDecimal(noOfDaysLastMonth), 2, RoundingMode.HALF_UP);
					}
				} else
					amount = new BigDecimal(0);
				totamt = totamt.add(amount);
				data += amount + ",";
			}

			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}

		s += "			{\n";
		s += "				label: '" + firstDateOfLastMonth.getMonth().toString() + ", "
				+ firstDateOfLastMonth.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.red,\n";
		s += "				borderColor: window.chartColors.red,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";

		label = "";
		data = "";
		amount = new BigDecimal(0, new MathContext(2));
		totamt = new BigDecimal(0, new MathContext(2));

		sql = "select xdiv,sum(xinvcount) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + firstDateOfLast2Month.getYear() + " and xper=" + firstDateOfLast2Month.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				if (amount.compareTo(BigDecimal.ZERO) > 0) {
					if (noOfDaysLast2Month > 0) {
						amount = amount.divide(new BigDecimal(noOfDaysLast2Month), 2, RoundingMode.HALF_UP);
					}
				} else
					amount = new BigDecimal(0);
				totamt = totamt.add(amount);
				data += amount + ",";
			}

			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}
		s += "			{\n";
		s += "				label: '" + firstDateOfLast2Month.getMonth().toString() + ", "
				+ firstDateOfLast2Month.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.grey,\n";
		s += "				borderColor: window.chartColors.grey,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";
		s += "		]\n";
		s += "		},\n";
		s += "		options: {\n";
		s += "			plugins: {\n";
		s += "				datalabels: {\n";
		s += "					rotation:270,\n";
		s += "					align:'top',\n";
		s += "					anchor:'end',\n";
		s += "					labels: {\n";
		s += "						title: {\n";
		s += "							color: 'black'\n";
		s += "						}\n";
		s += "					}\n";
		s += "				}\n";
		s += "			},\n";
		s += "			responsive: true,\n";
		s += "			title: {\n";
		s += "				display: true,\n";
		s += "				fontSize: 18,\n";
		s += "				text: 'Average Customer Count For " + previousday.getMonth().toString() + ", "
				+ previousday.getYear() + ", " + firstDateOfLastMonth.getMonth().toString() + ", "
				+ firstDateOfLastMonth.getYear() + ", " + firstDateOfLast2Month.getMonth().toString() + ", "
				+ firstDateOfLast2Month.getYear() + "'\n";
		s += "			},\n";
		s += "			tooltips: {\n";
		s += "				mode: 'index',\n";
		s += "				intersect: false,\n";
		s += "    			callbacks: {\n";
		s += "					label: function(tooltipItem, data){\n";
		s += "        				return addCommas(tooltipItem.yLabel);\n";
		s += "    				}\n";
		s += "    			}\n";
		s += "			},\n";
		s += "			hover: {\n";
		s += "				mode: 'nearest',\n";
		s += "				intersect: true\n";
		s += "			},\n";
		s += "			scales: {\n";
		s += "				xAxes: [{\n";
		s += "					display: true,\n";
		s += "					scaleLabel: {\n";
		s += "						display: false\n";
		s += "					}\n";
		s += "				}],\n";
		s += "				yAxes: [{\n";
		s += "					display: true,\n";
		s += "					scaleLabel: {\n";
		s += "						display: true,\n";
		s += "						labelString: 'Number '\n";
		s += "					},\n";
		s += "					ticks: {\n";
		s += "    					beginAtZero:true,\n";
		s += "    					callback: function(value) {\n";
		s += "        					return addCommas(value);\n";
		s += "    					}\n";
		s += "					}\n";
		s += "				}]\n";
		s += "			}\n";
		s += "		}\n";
		s += "	};\n";
		// ********************* END GRAPH FOR AVG CUSTOMER COUNT
		// ********************************//

		// ********************* GRAPH FOR AVG BASKET VALUE
		// ********************************//
		label = "";
		data = "";
		BigDecimal invcount = new BigDecimal(0, new MathContext(0));
		amount = new BigDecimal(0, new MathContext(0));
		totamt = new BigDecimal(0, new MathContext(0));

//		invoiceAmtount = SUM(xlineamt+xsupptaxamt+xvatamt-xdiscamt)-SUM(xcrnamt+xvoucheramt+xbonusamt)+SUM(xrounding)
//		grossAmount = SUM(xlineamt+xsupptaxamt+xvatamt-xdiscamt)
		sql = "select xdiv,SUM(xlineamt+xsupptaxamt+xvatamt-xdiscamt),sum(xinvcount) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + previousday.getYear() + " and xper=" + previousday.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				invcount = zabTools.getBigDecimal(resultrows[i][2]);
				if (amount.compareTo(BigDecimal.ZERO) > 0) {
					if (invcount.compareTo(BigDecimal.ZERO) > 0) {
						amount = amount.divide(invcount, 2, RoundingMode.HALF_UP);
					}
				} else
					amount = new BigDecimal(0);
				totamt = totamt.add(amount);
				data += amount + ",";
			}

			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}

		s += "var avgbasketvalueconfig = {\n";
		s += "		type: 'bar',\n";
		s += "		data: {\n";
		s += "			" + label + ",\n";
		s += "			datasets: [\n";
		s += "			{\n";
		s += "				label: '" + previousday.getMonth().toString() + ", " + previousday.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.blue,\n";
		s += "				borderColor: window.chartColors.blue,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";

		label = "";
		data = "";
		invcount = new BigDecimal(0, new MathContext(0));
		amount = new BigDecimal(0, new MathContext(0));
		totamt = new BigDecimal(0, new MathContext(0));

//		invoiceAmtount = SUM(xlineamt+xsupptaxamt+xvatamt-xdiscamt)-SUM(xcrnamt+xvoucheramt+xbonusamt)+SUM(xrounding)
//		grossAmount = SUM(xlineamt+xsupptaxamt+xvatamt-xdiscamt)
		sql = "select xdiv,SUM(xlineamt+xsupptaxamt+xvatamt-xdiscamt),sum(xinvcount) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + firstDateOfLastMonth.getYear() + " and xper=" + firstDateOfLastMonth.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				invcount = zabTools.getBigDecimal(resultrows[i][2]);
				if (amount.compareTo(BigDecimal.ZERO) > 0) {
					if (invcount.compareTo(BigDecimal.ZERO) > 0) {
						amount = amount.divide(invcount, 2, RoundingMode.HALF_UP);
					}
				} else
					amount = new BigDecimal(0);
				totamt = totamt.add(amount);
				data += amount + ",";
			}

			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}

		s += "			{\n";
		s += "				label: '" + firstDateOfLastMonth.getMonth().toString() + ", "
				+ firstDateOfLastMonth.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.red,\n";
		s += "				borderColor: window.chartColors.red,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";

		label = "";
		data = "";
		invcount = new BigDecimal(0, new MathContext(0));
		amount = new BigDecimal(0, new MathContext(0));
		totamt = new BigDecimal(0, new MathContext(0));

//		invoiceAmtount = SUM(xlineamt+xsupptaxamt+xvatamt-xdiscamt)-SUM(xcrnamt+xvoucheramt+xbonusamt)+SUM(xrounding)
//		grossAmount = SUM(xlineamt+xsupptaxamt+xvatamt-xdiscamt)
		sql = "select xdiv,SUM(xlineamt+xsupptaxamt+xvatamt-xdiscamt),sum(xinvcount) from oprptsalessum";
		sql += " where zid='" + id + "'";
		sql += " and xyear=" + firstDateOfLast2Month.getYear() + " and xper=" + firstDateOfLast2Month.getMonthValue();
		sql += " group by xdiv";
		sql += " order by xdiv";
		resultrows = zabinfo.getSqlRows(sql);
		if (resultrows == null) {
			label = "labels:[]";
			data = "data: []";
		} else {
			for (int i = 0; i < resultrows.length; i++) {
				label += "'" + resultrows[i][0] + "',";
				amount = zabTools.getBigDecimal(resultrows[i][1]);
				invcount = zabTools.getBigDecimal(resultrows[i][2]);
				if (amount.compareTo(BigDecimal.ZERO) > 0) {
					if (invcount.compareTo(BigDecimal.ZERO) > 0) {
						amount = amount.divide(invcount, 2, RoundingMode.HALF_UP);
					}
				} else
					amount = new BigDecimal(0);
				totamt = totamt.add(amount);
				data += amount + ",";
			}
			label = label + "'Total'";
			label = "labels:[" + label + "]";
			data = data + totamt.toString();
			data = "data:[" + data + "]";
		}

		s += "			{\n";
		s += "				label: '" + firstDateOfLast2Month.getMonth().toString() + ", "
				+ firstDateOfLast2Month.getYear() + "',\n";
		s += "				backgroundColor: window.chartColors.grey,\n";
		s += "				borderColor: window.chartColors.grey,\n";
		s += "				" + data + ",\n";
		s += "				fill: false,\n";
		s += "			},\n";
		s += "		]\n";
		s += "		},\n";
		s += "		options: {\n";
		s += "			plugins: {\n";
		s += "				datalabels: {\n";
		s += "					rotation:270,\n";
		s += "					align:'top',\n";
		s += "					anchor:'end',\n";
		s += "					labels: {\n";
		s += "						title: {\n";
		s += "							color: 'black'\n";
		s += "						}\n";
		s += "					}\n";
		s += "				}\n";
		s += "			},\n";
		s += "			responsive: true,\n";
		s += "			title: {\n";
		s += "				display: true,\n";
		s += "				fontSize: 18,\n";
		s += "				text: 'Average Basket Value For " + previousday.getMonth().toString() + ", "
				+ previousday.getYear() + ", " + firstDateOfLastMonth.getMonth().toString() + ", "
				+ firstDateOfLastMonth.getYear() + ", " + firstDateOfLast2Month.getMonth().toString() + ", "
				+ firstDateOfLast2Month.getYear() + "'\n";
		s += "			},\n";
		s += "			tooltips: {\n";
		s += "				mode: 'index',\n";
		s += "				intersect: false,\n";
		s += "    			callbacks: {\n";
		s += "					label: function(tooltipItem, data){\n";
		s += "        				return addCommas(tooltipItem.yLabel);\n";
		s += "    				}\n";
		s += "    			}\n";
		s += "			},\n";
		s += "			hover: {\n";
		s += "				mode: 'nearest',\n";
		s += "				intersect: true\n";
		s += "			},\n";
		s += "			scales: {\n";
		s += "				xAxes: [{\n";
		s += "					display: true,\n";
		s += "					scaleLabel: {\n";
		s += "						display: false\n";
		s += "					}\n";
		s += "				}],\n";
		s += "				yAxes: [{\n";
		s += "					display: true,\n";
		s += "					scaleLabel: {\n";
		s += "						display: true,\n";
		s += "						labelString: 'Number '\n";
		s += "					},\n";
		s += "					ticks: {\n";
		s += "    					beginAtZero:true,\n";
		s += "    					callback: function(value) {\n";
		s += "        					return addCommas(value);\n";
		s += "    					}\n";
		s += "					}\n";
		s += "				}]\n";
		s += "			}\n";
		s += "		}\n";
		s += "	};\n";

		// ********************* END GRAPH FOR AVG BASKET VALUE
		// ********************************//

		s += "	window.onload = function() {\n";
		s += "		var ctx = document.getElementById('netsales').getContext('2d');\n";
		s += "		var chart = new Chart(ctx, netsalesconfig);\n";
		s += "		var ctx1 = document.getElementById('avgsales').getContext('2d');\n";
		s += "		var chart1 = new Chart(ctx1, avgsalesconfig);\n";
		s += "		var ctx2 = document.getElementById('customercount').getContext('2d');\n";
		s += "		var chart2 = new Chart(ctx2, customercountonfig);\n";

		s += "		var ctx3 = document.getElementById('avgcustomercount').getContext('2d');\n";
		s += "		var chart3 = new Chart(ctx3, avgcustomercountconfig);\n";

		s += "		var ctx4 = document.getElementById('avgbasketvalue').getContext('2d');\n";
		s += "		var chart4 = new Chart(ctx4, avgbasketvalueconfig);\n";
		s += "	};\n";
		s += "							</script>\n";

		return s;
	}

}

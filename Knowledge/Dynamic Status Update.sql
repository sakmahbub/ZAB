SELECT        a.zid, a.xtornum, a.xfwh, b.xlong AS xdes1, a.xtwh, c.xlong AS xdes2, d.xitem, e.xdesc, d.xqtyreq, d.xpsize, d.xqtyord, d.xqtyalc, d.xqtycom, d.xqtypor, d.xrow, d.xserial, d.xstatus, d.xbkstatus, d.xunit, ISNULL(e.xcfpur, 1) 
                         AS xcfpur, f.xavail / ISNULL(e.xcfpur, 1) AS xavail, f.xavail AS xqty, d.xdate, '' AS Expr1, d.xstype, d.xbkqty, d.xlineamt, d.xrate, d.xvatrate, d.xbinref, d.xdphqty, d.xprepqty, 
                         CASE WHEN xavail < 0.01 THEN 'No Stock' WHEN xavail < (xdphqty - xqtyalc) AND xavail > 0 THEN 'Stock Partially available' WHEN xavail >= (xdphqty - xqtyalc) THEN 'Stock available' ELSE '' END AS xsptype
FROM            dbo.imtorheader AS a INNER JOIN
                         dbo.imtordetail AS d ON a.zid = d.zid AND a.xtornum = d.xtornum INNER JOIN
                         dbo.caitem AS e ON d.zid = e.zid AND d.xitem = e.xitem LEFT OUTER JOIN
                         dbo.branchview AS b ON a.zid = b.zid AND a.xfwh = b.xcode LEFT OUTER JOIN
                         dbo.branchview AS c ON a.zid = c.zid AND a.xtwh = c.xcode LEFT OUTER JOIN
                         dbo.imstock AS f ON a.zid = f.zid AND d.xitem = f.xitem AND a.xfwh = f.xwh
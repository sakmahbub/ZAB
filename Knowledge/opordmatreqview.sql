




SELECT        d.zid, d.xordernum, d.xitem, d.xqty, d.xunitsel, d.xunitpur, d.xgitem, d.xrate, d.xlineamt, d.xqtymix, d.xorderrow, d.xrow, d.xmitem, '' AS Expr1, d.xcolor, d.xpsize, h.xavail, h.xwh, d.xstyle, d.xconstruction, d.xcontent, d.xtype, 
                         CASE WHEN xavail < 0.01 THEN 'No Stock' WHEN xavail < xlineamt AND xavail > 0 THEN 'Stock Partially available' WHEN xavail >= xlineamt THEN 'Stock available' ELSE '' END AS xstatus
FROM            dbo.opordmatreq AS d INNER JOIN
                         dbo.imstock AS h ON d.zid = h.zid AND d.xitem = h.xitem
WHERE        (h.xavail <> 0)










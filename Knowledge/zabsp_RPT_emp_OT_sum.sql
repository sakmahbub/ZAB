USE [ZABDB]
GO
/****** Object:  StoredProcedure [dbo].[zabsp_RPT_emp_OT_sum]    Script Date: 9/25/2019 4:48:34 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--select * from pdattview where  xothours>'00:00:00'
-- zabsp_RPT_emp_OT_sum 100000,'','','','2019-09-01','2019-09-11','C5283','C5697'

ALTER PROC [dbo].[zabsp_RPT_emp_OT_sum] 
									 @zid INT
									,@dept VARCHAR(50)
									,@sec VARCHAR(50)
									,@subsec VARCHAR(50)
									,@fdate VARCHAR(50)
									,@tdate VARCHAR(50)
									,@fstaff VARCHAR(50)
									,@tstaff VARCHAR(50)
				   

AS
	DECLARE	@zorg VARCHAR(150),@madd VARCHAR(250),@staff VARCHAR(150),
			@name VARCHAR(150),@empcategory VARCHAR(50),@emptype VARCHAR(50),@sex VARCHAR(150),@designation VARCHAR(150),
			@deptname VARCHAR(50),@section VARCHAR(50),@subsector VARCHAR(50),
			@othours DECIMAL(20,2),@hdothours DECIMAL(20,2),@fhdothours DECIMAL(20,2),@rlvwrkhours DECIMAL(20,2)
					

	DECLARE @table TABLE(zid INT,zorg VARCHAR(150),madd VARCHAR(250),xstaff VARCHAR(150),
						 xname VARCHAR(150),xempcategory VARCHAR(50),xemptype VARCHAR(50),xsex VARCHAR(150),xdesignation VARCHAR(150),
						 xdeptname VARCHAR(50),xsection VARCHAR(50),xsubsector VARCHAR(50),
						 xothours DECIMAL(20,2),xhdothours DECIMAL(20,2),xfhdothours DECIMAL(20,2),xrlvwrkhours DECIMAL(20,2))

	SET @fdate = CAST(@fdate AS DATE)
	SET @tdate = CAST(@tdate AS DATE)

	select @zorg=zorg, @madd=xmadd from zbusiness WHERE zid=@zid

	DECLARE overtime_cursor CURSOR FORWARD_ONLY FOR 
		select p.xstaff from pdmst p
		WHERE p.zid=@zid
		AND p.xdeptname >= CASE WHEN @dept = '' THEN '' ELSE @dept END
		AND p.xdeptname <= CASE WHEN @dept = '' THEN 'zzz' ELSE @dept END
		AND p.xsection >= CASE WHEN @sec = '' THEN '' ELSE @sec END
		AND p.xsection <= CASE WHEN @sec = '' THEN 'zzz' ELSE @sec END
		AND p.xsubsector >= CASE WHEN @subsec = '' THEN '' ELSE @subsec END
		AND p.xsubsector <= CASE WHEN @subsec = '' THEN 'zzz' ELSE @subsec END
		AND p.xstaff>= CASE WHEN @fstaff = '' AND @tstaff = '' THEN '' ELSE @fstaff END
		AND p.xstaff<= CASE WHEN @fstaff = '' AND @tstaff = '' THEN 'zzz' ELSE @tstaff END

    OPEN overtime_cursor
    FETCH FROM overtime_cursor INTO  @staff
	WHILE @@FETCH_STATUS = 0
	BEGIN

		SET @othours = 0
		SET @hdothours = 0
		SET @fhdothours = 0
		SET @rlvwrkhours = 0

		SELECT @name=xname,@empcategory=xempcategory,@emptype=xemptype,@sex=xsex,@designation=xdesignation,@deptname=xdeptname,@section=xsection,@subsector=xsubsector FROM pdmst WHERE zid=@zid and xstaff=@staff

		----------------------------PD Transaction Detail for Normal Overtime--------------------------------
		select @othours=SUM(DATEDIFF(MINUTE, '00:00:00', xothours))/60.00  from pdattview WHERE zid=@zid AND xstaff=@staff AND xdate between @fdate AND @tdate
			and xyearperdate not in (select xyearperdate from pdcalenderdetail where zid=@zid and cast(xdate as date) between @fdate AND @tdate)		
		----------------------------End Of PD Transaction Detail for Normal Overtime--------------------------------

		----------------------------PD Transaction Detail for Holiday Overtime--------------------------------		
									/***SUMMATION OF WORKHOUR ADN OVERTIMEHOUR IN HOLIDAY WHEN APPLICABLE****************/
		select  @hdothours=ISNULL(SUM(ISNULL(DATEDIFF(MINUTE, '00:00:00', a.xothours),0)+ISNULL(DATEDIFF(MINUTE, '00:00:00', a.xworktime),0)),0)/60.00  from pdattview a join pdlvreplacetrn l on a.zid=l.zid and a.xstaff=l.xstaff and cast(a.xdate as date)=cast(l.xdate as date)
			WHERE a.zid=@zid AND a.xstaff=@staff AND a.xdate between @fdate AND @tdate and l.xtype='OVERTIME' and l.xsign=1
			and a.xyearperdate in (select xyearperdate from pdcalenderdetail where zid=@zid and cast(xdate as date) between @fdate AND @tdate AND xtypeholiday IN ('Day Off','Holiday','Festival Holiday'))
		
								/***OVERTIMEHOUR CALCULATE ON REPLACEMENT DUTY****************/
		select @rlvwrkhours=ISNULL(SUM(DATEDIFF(MINUTE, '00:00:00', a.xothours)),0)/60.00  from pdattview a join pdlvreplacetrn l on a.zid=l.zid and a.xstaff=l.xstaff and cast(a.xdate as date)=cast(l.xdate as date)
			WHERE a.zid=@zid AND a.xstaff=@staff AND a.xdate between @fdate AND @tdate and l.xtype='REPLACEMENT' and l.xsign=1
			and a.xyearperdate in (select xyearperdate from pdcalenderdetail where zid=@zid and cast(xdate as date) between @fdate AND @tdate AND xtypeholiday IN ('Day Off','Holiday'))
		SET @hdothours=ISNULL(@hdothours,0)+ISNULL(@rlvwrkhours,0)		
		----------------------------End Of PD Transaction Detail for Holiday Overtime--------------------------------

		----------------------------PD Transaction Detail for Festival Holiday Overtime--------------------------------		
								/***SUMMATION OF WORKHOUR AND OVERTIMEHOUR IN FESTIVAL HOLIDAY WHEN APPLICABLE****************/
		select  @fhdothours=ISNULL(SUM(ISNULL(DATEDIFF(MINUTE, '00:00:00', a.xothours),0)+ISNULL(DATEDIFF(MINUTE, '00:00:00', a.xworktime),0)),0)/60.00  from pdattview a join pdlvreplacetrn l on a.zid=l.zid and a.xstaff=l.xstaff and cast(a.xdate as date)=cast(l.xdate as date)
			WHERE a.zid=@zid AND a.xstaff=@staff AND a.xdate between @fdate AND @tdate and l.xtype='FESTIVAL' and l.xsign=1
			and a.xyearperdate in (select xyearperdate from pdcalenderdetail where zid=@zid and cast(xdate as date) between @fdate AND @tdate AND xtypeholiday='Festival Holiday')
		----------------------------END PD Transaction Detail for Festival Holiday Overtime--------------------------------		
			
		INSERT INTO @table(zid,zorg,madd,xstaff,xname,xempcategory,xemptype,xsex,xdesignation,xdeptname,xsection,xsubsector,xothours,xhdothours,xfhdothours,xrlvwrkhours)
					values(@zid,@zorg,@madd,@staff,@name,@empcategory,@emptype,@sex,@designation,@deptname,@section,@subsector,@othours,@hdothours,@fhdothours,@rlvwrkhours)

  	FETCH NEXT FROM overtime_cursor INTO @staff
	END
	CLOSE overtime_cursor
	DEALLOCATE overtime_cursor
	
	SELECT zid,zorg,madd,xstaff,xname,xempcategory,xemptype,xsex,xdesignation,xdeptname,xsection,xsubsector,xothours,xhdothours,xfhdothours,xrlvwrkhours FROM @table

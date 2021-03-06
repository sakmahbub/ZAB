USE [ZABDBICD]
GO
/****** Object:  StoredProcedure [dbo].[zabsp_CreatePDF_SendMail]    Script Date: 3/15/2021 1:08:31 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


--exec zabsp_CreatePDF_SendMail 100010,'Admin','2021-03-15','Payslip'

ALTER PROC [dbo].[zabsp_CreatePDF_SendMail] --#id,xcus,xdate
	@zid INT,
	@user VARCHAR(50),
	@date DATE,
	--@pcat VARCHAR(50),
	@action VARCHAR(50)
	

AS
	DECLARE 
		@totamt DECIMAL(20,2),
		@staff VARCHAR(50),
		@prime DECIMAL(20,2),
		@ordernum VARCHAR(50),
		@email VARCHAR(50)
		

IF @action = 'Payslip'
	BEGIN
		DECLARE main_cursor1 CURSOR FORWARD_ONLY FOR

		SELECT distinct (b.xstaff) FROM  pdmst  b
		WHERE b.zid = 100010
		AND b.xempstatus='Normal'
		AND b.xstaff='CID-0873'
		AND isnull(b.xoemail,'')not in('','.')
		order by b.xstaff

		OPEN main_cursor1
		FETCH FROM main_cursor1 INTO @staff	
		WHILE @@FETCH_STATUS = 0
		
			BEGIN
			IF Exists(select xoemail from pdmst where zid=@zid and xstaff=@staff  AND isnull(xoemail,'') not in('','.'))
			
			EXEC zabsp_sendmail_payslip @zid,@user,@staff,'Payslip Statement',@date,'Payslip'
	
			FETCH NEXT FROM main_cursor1 INTO @staff		
			END
		CLOSE main_cursor1
		DEALLOCATE main_cursor1
	END



ELSE Return
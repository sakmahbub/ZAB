USE [ZABDBICD]
GO
/****** Object:  StoredProcedure [dbo].[zabsp_sendmail_payslip]    Script Date: 3/15/2021 1:06:43 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER OFF
GO
--exec zabsp_sendmail_payslip 100010,'zabadmin','Admin','Payslip Statement','2021-03-15','Payslip'
ALTER proc [dbo].[zabsp_sendmail_payslip]
	 @zid INT
	,@user VARCHAR(50)
	,@reqnum VARCHAR(50)
	,@subject VARCHAR(50)
	,@pdate DATE
	,@action VARCHAR(50)
--	,@tomail VARCHAR(100)
	
AS
DECLARE
	@bodytext varchar(1500)
--	,@tomail varchar(100)
	,@request VARCHAR(50)
	,@issue VARCHAR(150)
	,@issue2 VARCHAR(150)
	,@business VARCHAR(100)
	,@profile VARCHAR(50)
	,@path VARCHAR(200)
	,@tomail VARCHAR(100)
	,@date DATE
	,@filename VARCHAR(100)
	,@cus VARCHAR(50)
	,@sex  VARCHAR(50)
	,@name  VARCHAR(100)

BEGIN

SET @date=@pdate
IF @reqnum='Admin'
	BEGIN
		SET @tomail='sakmahbub@gmail.com'--;zabnazrul'--
		SET @filename=@reqnum
		print @filename
	END
ELSE if left(@reqnum,2) in ('MR','PM')
	BEGIN
		Select  @tomail=isnull(xoemail,'') from pdmst where zid=@zid AND xstaff=@reqnum
		Select  @cus=xstaff from pdmst where zid=@zid AND xstaff=@reqnum
		SET @filename=CONVERT(VARCHAR, CAST(@date as date), 12)+@cus
		print @filename
	END
ELSE
	BEGIN
		Select  @tomail=isnull(xoemail,''),@sex=xsex,@name=xname from pdmst where zid=@zid AND xstaff=@reqnum
		If @sex='Male'
			set @name='Mr. '+@name
		 Else
			set @name='Mrs. '+@name

		SET @filename=@reqnum
		print @filename
	END


--SET @path='D:\portfolio\Jan_18\18010100247.pdf'--+CAST(@zid as VARCHAR(10))+'.pdf'
IF @action='Payslip'
	SET @path='D:\SendMail'+'\'+@filename+'.pdf'
ELSE 
	SET @path='D:\Send Mail\Confirmation'+'\'+@filename+'.pdf'

	
--print @path
--print @tomail

IF @tomail<>''
BEGIN
set @bodytext='Dear '+@name+','
			   + ' <br><font size="2" face="Comic Sans MS">Please find the attachement for your Payslip Information.'
			 
			   + '<br>'
			   + ' <br>Thanks,'
			   + ' <br>" InterContinental Dhaka."'
			   +'<br>1, Minto Road, Dhaka'
               +'<br>Tel: (+88-02)-55663030'
               +'<br>E-mail: dhaka@intercontinental-dhaka.com, www.intercontinental.com/dhaka'
			   + ' <br>__________________________________________________________'
			   + ' <br><font size="1" color="red">This is a system generated email, do not reply to this email id. For any queries fell free to contact us.</font>'

EXEC msdb.dbo.sp_send_dbmail
    @profile_name ='ZAB_eMail',--@profile
    @recipients =@tomail,
    @body = @bodytext,
    @subject = @subject,
    @importance ='Normal', 
    @body_format='HTML',
    @file_attachments=@path;
SET @name=''

--END

END
END



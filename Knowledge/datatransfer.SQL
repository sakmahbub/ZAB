USE [ZABDB]
GO
/****** Object:  StoredProcedure [dbo].[Data_Transfer]    Script Date: 12/22/2019 10:23:47 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER OFF
GO

--exec Data_Transfer 100000,'zabadmin'

ALTER PROCEDURE [dbo].[Data_Transfer]
				@zid INT,
				@user varchar(150)
AS
	DECLARE  @size VARCHAR(50),
			@descs VARCHAR(250)
			

	

	DECLARE cursor_bulk CURSOR FORWARD_ONLY FOR
		SELECT size,descs
		FROM temprodsize
		WHERE descs IS NOT NULL
		
	OPEN cursor_bulk
	FETCH FROM cursor_bulk INTO  @size,@descs
	WHILE @@FETCH_STATUS = 0
	BEGIN
 


	

				INSERT INTO xcodes(zid,ztime,zactive,xemail,xtype,xcode)
				VALUES(@zid,GETDATE(),1,'',@size,@descs)		
		  


		FETCH NEXT FROM cursor_bulk INTO  @size,@descs
		END
	CLOSE cursor_bulk
	DEALLOCATE cursor_bulk


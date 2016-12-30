//package com.arraybit.global;
//
///**
// * Created by Avani on 30/12/2016.
// */
//
//public class sqlquery {
//    USE [master]
//    GO
//    /****** Object:  Database [abMYM]    Script Date: 30/12/2016 1:04:01 PM ******/
//    CREATE DATABASE [abMYM] ON  PRIMARY
//            ( NAME = N'abMYM', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL10_50.MSSQLSERVER\MSSQL\DATA\abMYM.mdf' , SIZE = 3072KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
//    LOG ON
//            ( NAME = N'abMYM_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL10_50.MSSQLSERVER\MSSQL\DATA\abMYM_log.ldf' , SIZE = 1536KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
//    GO
//    ALTER DATABASE [abMYM] SET COMPATIBILITY_LEVEL = 100
//    GO
//    IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
//    begin
//    EXEC [abMYM].[dbo].[sp_fulltext_database] @action = 'enable'
//    end
//            GO
//    ALTER DATABASE [abMYM] SET ANSI_NULL_DEFAULT OFF
//            GO
//    ALTER DATABASE [abMYM] SET ANSI_NULLS OFF
//            GO
//    ALTER DATABASE [abMYM] SET ANSI_PADDING OFF
//            GO
//    ALTER DATABASE [abMYM] SET ANSI_WARNINGS OFF
//            GO
//    ALTER DATABASE [abMYM] SET ARITHABORT OFF
//            GO
//    ALTER DATABASE [abMYM] SET AUTO_CLOSE OFF
//            GO
//    ALTER DATABASE [abMYM] SET AUTO_CREATE_STATISTICS ON
//            GO
//    ALTER DATABASE [abMYM] SET AUTO_SHRINK OFF
//            GO
//    ALTER DATABASE [abMYM] SET AUTO_UPDATE_STATISTICS ON
//            GO
//    ALTER DATABASE [abMYM] SET CURSOR_CLOSE_ON_COMMIT OFF
//            GO
//    ALTER DATABASE [abMYM] SET CURSOR_DEFAULT  GLOBAL
//            GO
//    ALTER DATABASE [abMYM] SET CONCAT_NULL_YIELDS_NULL OFF
//            GO
//    ALTER DATABASE [abMYM] SET NUMERIC_ROUNDABORT OFF
//            GO
//    ALTER DATABASE [abMYM] SET QUOTED_IDENTIFIER OFF
//            GO
//    ALTER DATABASE [abMYM] SET RECURSIVE_TRIGGERS OFF
//            GO
//    ALTER DATABASE [abMYM] SET  DISABLE_BROKER
//    GO
//    ALTER DATABASE [abMYM] SET AUTO_UPDATE_STATISTICS_ASYNC OFF
//            GO
//    ALTER DATABASE [abMYM] SET DATE_CORRELATION_OPTIMIZATION OFF
//            GO
//    ALTER DATABASE [abMYM] SET TRUSTWORTHY OFF
//            GO
//    ALTER DATABASE [abMYM] SET ALLOW_SNAPSHOT_ISOLATION OFF
//            GO
//    ALTER DATABASE [abMYM] SET PARAMETERIZATION SIMPLE
//            GO
//    ALTER DATABASE [abMYM] SET READ_COMMITTED_SNAPSHOT OFF
//            GO
//    ALTER DATABASE [abMYM] SET HONOR_BROKER_PRIORITY OFF
//            GO
//    ALTER DATABASE [abMYM] SET RECOVERY SIMPLE
//            GO
//    ALTER DATABASE [abMYM] SET  MULTI_USER
//    GO
//    ALTER DATABASE [abMYM] SET PAGE_VERIFY CHECKSUM
//            GO
//    ALTER DATABASE [abMYM] SET DB_CHAINING OFF
//            GO
//    USE [abMYM]
//    GO
//    /****** Object:  User [ab]    Script Date: 30/12/2016 1:04:01 PM ******/
//    CREATE USER [ab] FOR LOGIN [ab] WITH DEFAULT_SCHEMA=[dbo]
//    GO
//    ALTER ROLE [db_owner] ADD MEMBER [ab]
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymAdvertiseMaster_Delete]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymAdvertiseMaster_Delete]
//    @AdvertiseMasterId int
//    ,@linktoMemberMasterIdUpdatedBy int
//    ,@UpdateDateTime datetime
//    ,@IsDeleted bit
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//
//    UPDATE
//            mymAdvertiseMaster
//    SET
//    linktoMemberMasterIdUpdatedBy=@linktoMemberMasterIdUpdatedBy
//    ,UpdateDateTime=@UpdateDateTime
//    ,IsDeleted=@IsDeleted
//    WHERE
//            AdvertiseMasterId = @AdvertiseMasterId
//
//            IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymAdvertiseMaster_Insert]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymAdvertiseMaster_Insert]
//    @AdvertiseMasterId int OUTPUT
//    ,@AdvertiseText varchar(500) = NULL
//    ,@AdvertiseImageName varchar(100) = NULL
//    ,@WebsiteURL varchar(150) = NULL
//    ,@AdvertisementType varchar(10)
//    ,@linktoMemberMasterIdCreatedBy int
//    ,@CreateDateTime datetime
//    ,@IsEnabled bit
//    ,@IsDeleted bit
//
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//    SET NOCOUNT OFF
//
//    IF EXISTS(SELECT AdvertiseMasterId FROM mymAdvertiseMaster WHERE AdvertiseMasterId = @AdvertiseMasterId AND IsDeleted = 0)
//    BEGIN
//    SELECT @AdvertiseMasterId = AdvertiseMasterId FROM mymAdvertiseMaster WHERE AdvertiseMasterId = @AdvertiseMasterId
//    SET @Status = -2
//    RETURN
//            END
//    INSERT INTO mymAdvertiseMaster
//            (
//                    AdvertiseText
//                    ,AdvertiseImageName
//                    ,WebsiteURL
//                    ,AdvertisementType
//                    ,linktoMemberMasterIdCreatedBy
//                    ,CreateDateTime
//                    ,IsEnabled
//                    ,IsDeleted
//
//                    )
//    VALUES
//            (
//                    @AdvertiseText
//                    ,@AdvertiseImageName
//                    ,@WebsiteURL
//                    ,@AdvertisementType
//                    ,@linktoMemberMasterIdCreatedBy
//                    ,@CreateDateTime
//                    ,@IsEnabled
//                    ,@IsDeleted
//
//                    )
//
//    IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @AdvertiseMasterId = @@IDENTITY
//    SET @Status = 0
//    END
//
//            RETURN
//    END
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymAdvertiseMaster_Update]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymAdvertiseMaster_Update]
//    @AdvertiseMasterId int
//    ,@AdvertiseText varchar(500) = NULL
//    ,@AdvertiseImageName varchar(100) = NULL
//    ,@WebsiteURL varchar(150) = NULL
//    ,@AdvertisementType varchar(10)
//    ,@linktoMemberMasterIdUpdatedBy int = NULL
//    ,@UpdateDateTime datetime = NULL
//
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//    SET NOCOUNT OFF
//
//    IF EXISTS(SELECT AdvertiseMasterId FROM mymAdvertiseMaster WHERE AdvertiseMasterId = @AdvertiseMasterId AND AdvertiseMasterId != @AdvertiseMasterId AND IsDeleted = 0)
//    BEGIN
//    SET @Status = -2
//    RETURN
//            END
//    UPDATE mymAdvertiseMaster
//    SET
//    AdvertiseText = @AdvertiseText
//    ,AdvertiseImageName = @AdvertiseImageName
//    ,WebsiteURL = @WebsiteURL
//    ,AdvertisementType = @AdvertisementType
//    ,linktoMemberMasterIdUpdatedBy = @linktoMemberMasterIdUpdatedBy
//    ,UpdateDateTime = @UpdateDateTime
//
//    WHERE
//            AdvertiseMasterId = @AdvertiseMasterId
//
//            IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//
//            RETURN
//    END
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymAdvertiseMasterDisableByAdvertiseMasterId_Update]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymAdvertiseMasterDisableByAdvertiseMasterId_Update]
//    @AdvertiseMasterId int
//    ,@linktoMemberMasterIdUpdatedBy int
//    ,@UpdateDateTime datetime
//    ,@IsEnabled bit
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//    IF EXISTS(SELECT AdvertiseMasterId FROM mymAdvertiseMaster WHERE AdvertiseMasterId = @AdvertiseMasterId AND AdvertiseMasterId != @AdvertiseMasterId)
//    BEGIN
//    SET @Status = -2
//    RETURN
//            END
//    UPDATE
//            mymAdvertiseMaster
//    SET
//    linktoMemberMasterIdUpdatedBy=@linktoMemberMasterIdUpdatedBy
//    ,UpdateDateTime=@UpdateDateTime
//    ,IsEnabled=@IsEnabled
//    WHERE
//            AdvertiseMasterId = @AdvertiseMasterId
//
//            IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymAdvertiseMasterPageWise_SelectAll]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymAdvertiseMasterPageWise_SelectAll]
//    @IsEnabled bit = NULL
//
//    ,@StartRowIndex int
//    ,@PageSize int
//    ,@TotalRowCount int OUTPUT
//    AS
//            BEGIN
//
//    SET NOCOUNT ON
//
//            SELECT
//    @TotalRowCount = COUNT(*)
//    FROM
//            mymAdvertiseMaster
//    WHERE
//            IsEnabled = ISNULL(@IsEnabled, IsEnabled)
//    AND IsDeleted = 0
//
//    SELECT * FROM
//            (
//                    SELECT ROW_NUMBER() OVER(ORDER BY AdvertiseMasterId) AS RowNumber
//    ,mymAdvertiseMaster.*
//    FROM
//            mymAdvertiseMaster
//    WHERE
//            IsEnabled = ISNULL(@IsEnabled, IsEnabled)
//    AND IsDeleted = 0
//    )
//    AS TEMP_TABLE
//    WHERE RowNumber BETWEEN @StartRowIndex + 1 AND @StartRowIndex + @PageSize
//
//    RETURN
//            END
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymErrorLog_Insert]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymErrorLog_Insert]
//    @ErrorLogId smallint OUTPUT
//    ,@ErrorDateTime datetime
//    ,@ErrorMessage varchar(500)
//    ,@ErrorStackTrace varchar(4000)
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//
//    SET NOCOUNT ON;
//    INSERT INTO mymErrorLog
//            (
//                    ErrorDateTime
//                    ,ErrorMessage
//                    ,ErrorStackTrace
//                    )
//    VALUES
//            (
//                    @ErrorDateTime
//                    ,@ErrorMessage
//                    ,@ErrorStackTrace
//                    )
//
//    IF @@ERROR <> 0
//    SET @Status = -1
//    ELSE
//            BEGIN
//    SET @ErrorLogId = @@IDENTITY
//    SET @Status = 0
//    END
//
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMaster_Insert]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymMemberMaster_Insert]
//    @MemberMasterId int OUTPUT
//    ,@MemberName varchar(150)
//    ,@ImageName varchar(100) = NULL
//    ,@Phone1 varchar(15)
//    ,@Phone2 varchar(15) = NULL
//    ,@Email varchar(80) = NULL
//    ,@Password varchar(25)
//    ,@MemberType varchar(10)
//    ,@LastLoginDateTime datetime
//    ,@Gender varchar(6)
//    ,@BirthDate date = NULL
//    ,@IsApproved bit = NULL
//    ,@FCMToken varchar(250) = NULL
//
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//    SET NOCOUNT OFF
//
//    IF EXISTS(SELECT MemberMasterId FROM mymMemberMaster WHERE Email = @Email)
//    BEGIN
//    SELECT @MemberMasterId = MemberMasterId FROM mymMemberMaster WHERE Email = @Email
//    SET @Status = -2
//    RETURN
//            END
//    INSERT INTO mymMemberMaster
//            (
//                    MemberName
//                    ,ImageName
//                    ,Phone1
//                    ,Phone2
//                    ,Email
//                    ,Password
//                    ,MemberType
//                    ,LastLoginDateTime
//                    ,Gender
//                    ,BirthDate
//                    ,IsApproved
//                    ,FCMToken
//
//                    )
//    VALUES
//            (
//                    @MemberName
//                    ,@ImageName
//                    ,@Phone1
//                    ,@Phone2
//                    ,@Email
//                    ,@Password
//                    ,@MemberType
//                    ,@LastLoginDateTime
//                    ,@Gender
//                    ,@BirthDate
//                    ,@IsApproved
//                    ,@FCMToken
//
//                    )
//
//    IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @MemberMasterId = @@IDENTITY
//    SET @Status = 0
//    END
//
//            RETURN
//    END
//
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMaster_SelectAll]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMaster_SelectAll]
//
//    AS
//            BEGIN
//
//    SET NOCOUNT ON;
//
//    SELECT
//    mymMemberMaster.*
//    FROM
//            mymMemberMaster
//    WHERE
//            IsApproved = 1
//    AND IsDeleted = 0
//    END
//
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterAdmin_Update]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMasterAdmin_Update]
//    @MemberMasterId int
//    ,@MemberType varchar(10)
//    ,@linktoMemberMasterIdUpdatedBy int
//    ,@UpdateDateTime datetime = NULL
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//
//    SET NOCOUNT ON;
//
//    IF NOT EXISTS(SELECT MemberMasterId FROM mymMemberMaster WHERE MemberMasterId = @MemberMasterId AND IsDeleted = 0 )
//    BEGIN
//    SET @Status = -2
//    RETURN
//            END
//    UPDATE mymMemberMaster
//    SET
//    MemberType = @MemberType
//    ,linktoMemberMasterIdUpdatedBy = @linktoMemberMasterIdUpdatedBy
//    ,UpdateDateTime = @UpdateDateTime
//    WHERE
//            MemberMasterId = @MemberMasterId
//
//            IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterByEmail_Select]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMasterByEmail_Select]
//    @Email varchar(50)
//
//    AS
//            BEGIN
//    SET NOCOUNT ON
//
//            SELECT
//    mymMemberMaster.*
//    FROM
//            mymMemberMaster
//    WHERE
//            Email = @Email
//            AND IsDeleted = 0
//
//    END
//
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterByMemberMasterId_Delete]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMasterByMemberMasterId_Delete]
//    @MemberMasterId int
//    ,@IsDeleted bit = NULL
//    ,@linktoMemberMasterIdUpdatedBy int = NULL
//    ,@UpdateDateTime datetime = NULL
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//
//    SET NOCOUNT ON;
//    IF NOT EXISTS(SELECT MemberMasterId FROM mymMemberMaster WHERE MemberMasterId = @MemberMasterId)
//    BEGIN
//    SET @Status = -2
//    RETURN
//            END
//    UPDATE mymMemberMaster
//    SET
//    IsDeleted= @IsDeleted
//    ,linktoMemberMasterIdUpdatedBy = @linktoMemberMasterIdUpdatedBy
//    ,UpdateDateTime = @UpdateDateTime
//    WHERE
//            MemberMasterId = @MemberMasterId
//
//            IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//
//            RETURN
//    END
//
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterByMembermasterId_Select]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymMemberMasterByMembermasterId_Select]
//    @MemberMasterId int
//            AS
//    BEGIN
//    SET NOCOUNT ON
//
//            SELECT
//    mymMemberMaster.*
//    FROM
//            mymMemberMaster
//    WHERE
//            MemberMasterId = @MemberMasterId
//            AND IsDeleted = 0
//
//    RETURN
//            END
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterByMemebermasterId_Update]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMasterByMemebermasterId_Update]
//    @MemberMasterId int
//    ,@MemberName varchar(150)
//    ,@Phone1 varchar(15)
//    ,@Phone2 varchar(15)
//    ,@Gender varchar(6)
//    ,@BirthDate date = NULL
//    ,@linktoMemberMasterIdUpdatedBy int = NULL
//    ,@UpdateDateTime datetime = NULL
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//
//    SET NOCOUNT ON;
//
//    UPDATE mymMemberMaster
//    SET
//    MemberName = @MemberName
//    ,Phone1= @Phone1
//    ,Phone2= @Phone2
//    ,Gender= @Gender
//    ,BirthDate = @BirthDate
//    ,linktoMemberMasterIdUpdatedBy = @MemberMasterId
//    ,UpdateDateTime = @UpdateDateTime
//    WHERE
//            MemberMasterId = @MemberMasterId
//
//            IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterContactDetail_Update]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymMemberMasterContactDetail_Update]
//    @MemberMasterId int
//    ,@LastLoginDateTime datetime
//    ,@HomeCountry varchar(50) = NULL
//    ,@HomeState varchar(50) = NULL
//    ,@HomeCity varchar(50) = NULL
//    ,@HomeArea varchar(50) = NULL
//    ,@HomeNearBy varchar(100) = NULL
//    ,@HomeNumberStreet varchar(50) = NULL
//    ,@HomeZipCode varchar(10) = NULL
//    ,@HomePhone varchar(80) = NULL
//    ,@OfficeCountry varchar(50) = NULL
//    ,@OfficeState varchar(50) = NULL
//    ,@OfficeCity varchar(50) = NULL
//    ,@OfficeArea varchar(50) = NULL
//    ,@OfficeNearBy varchar(100) = NULL
//    ,@OfficeNumberStreet varchar(50) = NULL
//    ,@OfficeZipCode varchar(10) = NULL
//    ,@OfficePhone varchar(80) = NULL
//    ,@linktoMemberMasterIdUpdatedBy int = NULL
//    ,@UpdateDateTime datetime = NULL
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//    SET NOCOUNT OFF
//
//    IF NOT EXISTS(SELECT MemberMasterId FROM mymMemberMaster WHERE MemberMasterId = @MemberMasterId AND IsDeleted = 0 )
//    BEGIN
//    SET @Status = -2
//    RETURN
//            END
//    UPDATE mymMemberMaster
//    SET
//    LastLoginDateTime = @LastLoginDateTime
//    ,HomeCountry = @HomeCountry
//    ,HomeState = @HomeState
//    ,HomeCity = @HomeCity
//    ,HomeArea = @HomeArea
//    ,HomeNearBy = @HomeNearBy
//    ,HomeNumberStreet = @HomeNumberStreet
//    ,HomeZipCode = @HomeZipCode
//    ,HomePhone = @HomePhone
//    ,OfficeCountry = @OfficeCountry
//    ,OfficeState = @OfficeState
//    ,OfficeCity = @OfficeCity
//    ,OfficeArea = @OfficeArea
//    ,OfficeNearBy= @OfficeNearBy
//    ,OfficeNumberStreet = @OfficeNumberStreet
//    ,OfficeZipCode = @OfficeZipCode
//    ,OfficePhone = @OfficePhone
//    ,linktoMemberMasterIdUpdatedBy = @linktoMemberMasterIdUpdatedBy
//    ,UpdateDateTime = @UpdateDateTime
//    WHERE
//            MemberMasterId = @MemberMasterId
//
//            IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//
//            RETURN
//    END
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterDetail_Insert]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMasterDetail_Insert]
//    @MemberMasterId int OUTPUT
//    ,@MemberName varchar(150)
//    ,@ImageName varchar(100) = NULL
//    ,@Phone1 varchar(15)
//    ,@Phone2 varchar(15) = NULL
//    ,@Email varchar(80) = NULL
//    ,@Password varchar(25)
//    ,@MemberType varchar(10)
//    ,@LastLoginDateTime datetime
//    ,@Gender varchar(6)
//    ,@BirthDate date = NULL
//    ,@IsApproved bit = NULL
//    ,@FCMToken varchar(250) = NULL
//    ,@Qualification varchar(50)
//    ,@BloodGroup varchar(3) = NULL
//    ,@Profession varchar(150) = NULL
//    ,@AnniversaryDate date = NULL
//    ,@UpdateDateTime datetime = NULL
//    ,@HomeCountry varchar(50) = NULL
//    ,@HomeState varchar(50) = NULL
//    ,@HomeCity varchar(50) = NULL
//    ,@HomeArea varchar(50) = NULL
//    ,@HomeNearBy varchar(100) = NULL
//    ,@HomeNumberStreet varchar(50) = NULL
//    ,@HomeZipCode varchar(10) = NULL
//    ,@HomePhone varchar(80) = NULL
//    ,@OfficeCountry varchar(50) = NULL
//    ,@OfficeState varchar(50) = NULL
//    ,@OfficeCity varchar(50) = NULL
//    ,@OfficeArea varchar(50) = NULL
//    ,@OfficeNearBy varchar(100) = NULL
//    ,@OfficeNumberStreet varchar(50) = NULL
//    ,@OfficeZipCode varchar(10) = NULL
//    ,@OfficePhone varchar(80) = NULL
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//
//    DECLARE
//    @IsDelete bit = 0
//
//    SET NOCOUNT ON;
//
//    IF EXISTS(SELECT MemberMasterId FROM mymMemberMaster WHERE Email = @Email AND IsDeleted = 0)
//    BEGIN
//    SELECT @MemberMasterId = MemberMasterId FROM mymMemberMaster WHERE Email = @Email AND IsDeleted = 0
//    SET @Status = -2
//    RETURN
//            END
//
//    INSERT INTO mymMemberMaster
//            (
//                    MemberName
//                    ,ImageName
//                    ,Phone1
//                    ,Phone2
//                    ,Email
//                    ,Password
//                    ,MemberType
//                    ,LastLoginDateTime
//                    ,Gender
//                    ,BirthDate
//                    ,IsApproved
//                    ,Qualification
//                    ,BloodGroup
//                    ,Profession
//                    ,AnniversaryDate
//                    ,UpdateDateTime
//                    ,HomeCountry
//                    ,HomeState
//                    ,HomeCity
//                    ,HomeArea
//                    ,HomeNearBy
//                    ,HomeNumberStreet
//                    ,HomeZipCode
//                    ,HomePhone
//                    ,OfficeCountry
//                    ,OfficeState
//                    ,OfficeCity
//                    ,OfficeArea
//                    ,OfficeNearBy
//                    ,OfficeNumberStreet
//                    ,OfficeZipCode
//                    ,OfficePhone
//                    ,IsDeleted
//
//                    )
//    VALUES
//            (
//                    @MemberName
//                    ,@ImageName
//                    ,@Phone1
//                    ,@Phone2
//                    ,@Email
//                    ,@Password
//                    ,@MemberType
//                    ,@LastLoginDateTime
//                    ,@Gender
//                    ,@BirthDate
//                    ,@IsApproved
//                    ,@Qualification
//                    ,@BloodGroup
//                    ,@Profession
//                    ,@AnniversaryDate
//                    ,@UpdateDateTime
//                    ,@HomeCountry
//                    ,@HomeState
//                    ,@HomeCity
//                    ,@HomeArea
//                    ,@HomeNearBy
//                    ,@HomeNumberStreet
//                    ,@HomeZipCode
//                    ,@HomePhone
//                    ,@OfficeCountry
//                    ,@OfficeState
//                    ,@OfficeCity
//                    ,@OfficeArea
//                    ,@OfficeNearBy
//                    ,@OfficeNumberStreet
//                    ,@OfficeZipCode
//                    ,@OfficePhone
//                    ,@IsDelete
//
//                    )
//
//    IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @MemberMasterId = @@IDENTITY
//
//    EXEC mymMemberMasterFCMToken_Update @FCMToken, @MemberMasterId, @Status
//
//    IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//            END
//    RETURN
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterFCMToken_Update]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMasterFCMToken_Update]
//    @FCMToken varchar(250) = NULL,
//    @MemberMasterId int,
//    @Status smallint OUTPUT
//    AS
//            BEGIN
//    UPDATE
//            mymMemberMaster
//    SET
//            FCMToken = NULL
//    WHERE
//            FCMToken = @FCMToken
//
//            UPDATE
//    mymMemberMaster
//            SET
//    FCMToken = @FCMToken
//    WHERE
//            MemberMasterId = @MemberMasterId
//
//            IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//            RETURN
//    END
//
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterFilterPageWise_SelectAll]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMasterFilterPageWise_SelectAll]
//    @MemberMasterId int
//    ,@Membername varchar(50) = NULL
//    ,@Profession varchar(150)= NULL
//    ,@Qualification varchar(50)= NULL
//    ,@BloodGroup varchar(3)= NULL
//
//    ,@StartRowIndex int
//    ,@PageSize int
//    ,@TotalRowCount int OUTPUT
//    AS
//            BEGIN
//
//    SET NOCOUNT ON;
//
//    SELECT
//    @TotalRowCount = COUNT(*)
//    FROM
//            mymMemberMaster
//    WHERE
//    MemberName LIKE ISNULL(@MemberName,'') + '%'
//    AND Profession = ISNULL(@Profession, Profession)
//    AND Qualification LIKE '%' + ISNULL(@Qualification, Qualification) + '%'
//    AND BloodGroup= ISNULL(@BloodGroup, BloodGroup)
//    AND IsApproved = 1
//    AND MemberMasterId != @MemberMasterId
//    AND IsDeleted = 0
//
//    SELECT * FROM
//            (
//                    SELECT ROW_NUMBER() OVER(ORDER BY MemberName) AS RowNumber
//    ,mymMemberMaster.*
//    FROM
//            mymMemberMaster
//    WHERE
//    MemberName LIKE ISNULL(@MemberName,'') + '%'
//    AND Profession = ISNULL(@Profession, Profession)
//    AND Qualification LIKE '%' + ISNULL(@Qualification, Qualification) + '%'
//    AND BloodGroup= ISNULL(@BloodGroup, BloodGroup)
//    AND IsApproved = 1
//    AND MemberMasterId != @MemberMasterId
//    AND IsDeleted = 0
//    )
//    AS TEMP_TABLE
//    WHERE RowNumber BETWEEN @StartRowIndex + 1 AND @StartRowIndex + @PageSize
//
//    END
//
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterImageByMemebermasterId_Update]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMasterImageByMemebermasterId_Update]
//    @MemberMasterId int
//    ,@ImageName varchar(100) = NULL
//    ,@linktoMemberMasterIdUpdatedBy int = NULL
//    ,@UpdateDateTime datetime = NULL
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//
//    SET NOCOUNT ON;
//
//    UPDATE mymMemberMaster
//    SET
//    ImageName= @ImageName
//    ,linktoMemberMasterIdUpdatedBy = @MemberMasterId
//    ,UpdateDateTime = @UpdateDateTime
//    WHERE
//            MemberMasterId = @MemberMasterId
//
//            IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterIsApproved_Update]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymMemberMasterIsApproved_Update]
//    @MemberMasterId int
//    ,@IsApproved bit = NULL
//    ,@linktoMemberMasterIdApprovedBy int
//    ,@ApprovedDateTime datetime
//    ,@linktoMemberMasterIdUpdatedBy int = NULL
//    ,@UpdateDateTime datetime = NULL
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//    SET NOCOUNT OFF
//
//    IF NOT EXISTS(SELECT MemberMasterId FROM mymMemberMaster WHERE MemberMasterId = @MemberMasterId AND IsDeleted = 0)
//    BEGIN
//    SET @Status = -2
//    RETURN
//            END
//    UPDATE mymMemberMaster
//    SET
//    IsApproved = @IsApproved
//    ,linktoMemberMasterIdApprovedBy = @linktoMemberMasterIdApprovedBy
//    ,ApprovedDateTime = @ApprovedDateTime
//    ,linktoMemberMasterIdUpdatedBy = @linktoMemberMasterIdUpdatedBy
//    ,UpdateDateTime = @UpdateDateTime
//    WHERE
//            MemberMasterId = @MemberMasterId
//
//            IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//
//            RETURN
//    END
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterLastLoginDateTime_Update]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMasterLastLoginDateTime_Update]
//    @MemberMasterId smallint
//    ,@LastLoginDateTime DateTime
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//
//    SET NOCOUNT ON;
//
//    UPDATE
//            mymMemberMaster
//    SET
//            LastLoginDateTime = @LastLoginDateTime
//            WHERE
//    MemberMasterId = @MemberMasterId
//
//    IF @@ERROR <> 0
//    SET @Status = -1
//    ELSE
//    SET @Status = 0
//
//    RETURN
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterLogout_Update]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMasterLogout_Update]
//    @MemberMasterId int
//    ,@linktoMemberMasterIdUpdatedBy int
//    ,@FCMToken varchar = NULL
//    ,@UpdateDateTime datetime = NULL
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//
//    SET NOCOUNT ON;
//
//    IF NOT EXISTS(SELECT MemberMasterId FROM mymMemberMaster WHERE MemberMasterId = @MemberMasterId AND IsDeleted = 0)
//    BEGIN
//    SET @Status = -3
//    RETURN
//            END
//
//    UPDATE mymMemberMaster
//    SET
//    FCMToken = @FCMToken
//    ,linktoMemberMasterIdUpdatedBy = @linktoMemberMasterIdUpdatedBy
//    ,UpdateDateTime = @UpdateDateTime
//    WHERE
//            MemberMasterId = @MemberMasterId
//
//            IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterNewPageWise_SelectAll]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymMemberMasterNewPageWise_SelectAll]
//    @StartRowIndex int
//    ,@PageSize int
//    ,@TotalRowCount int OUTPUT
//    AS
//            BEGIN
//
//    SET NOCOUNT ON
//
//            SELECT
//    @TotalRowCount = COUNT(*)
//    FROM
//            mymMemberMaster
//    WHERE
//    IsApproved IS NULL
//    AND IsDeleted = 0
//
//
//    SELECT * FROM
//            (
//                    SELECT ROW_NUMBER() OVER(ORDER BY MemberMasterId) AS RowNumber
//    ,mymMemberMaster.*
//    FROM
//            mymMemberMaster
//    WHERE
//    IsApproved IS NULL
//    AND IsDeleted = 0
//
//    )
//    AS TEMP_TABLE
//    WHERE RowNumber BETWEEN @StartRowIndex + 1 AND @StartRowIndex + @PageSize
//
//    RETURN
//            END
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterPageWise_SelectAll]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymMemberMasterPageWise_SelectAll]
//    @MemberMasterId int
//    ,@StartRowIndex int
//    ,@PageSize int
//    ,@TotalRowCount int OUTPUT
//    AS
//            BEGIN
//
//    SET NOCOUNT ON
//
//            SELECT
//    @TotalRowCount = COUNT(*)
//    FROM
//            mymMemberMaster
//    WHERE
//            IsApproved = 1
//    AND MemberMasterId != @MemberMasterId
//    AND IsDeleted = 0
//
//    SELECT * FROM
//            (
//                    SELECT ROW_NUMBER() OVER(ORDER BY MemberMasterId) AS RowNumber
//    ,mymMemberMaster.*
//    FROM
//            mymMemberMaster
//    WHERE
//            IsApproved = 1
//    AND MemberMasterId != @MemberMasterId
//    AND IsDeleted = 0
//    )
//    AS TEMP_TABLE
//    WHERE RowNumber BETWEEN @StartRowIndex + 1 AND @StartRowIndex + @PageSize
//
//    RETURN
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterPassword_Update]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMasterPassword_Update]
//    @MemberMasterId int
//    ,@Password varchar(10)
//    ,@ConfirmPassword varchar(10)
//    ,@linktoMemberMasterIdUpdatedBy int
//    ,@UpdateDateTime datetime = NULL
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//
//    SET NOCOUNT ON;
//
//    IF NOT EXISTS(SELECT MemberMasterId FROM mymMemberMaster WHERE MemberMasterId = @MemberMasterId AND Password = @Password AND IsDeleted = 0)
//    BEGIN
//    SET @Status = -3
//    RETURN
//            END
//
//    UPDATE mymMemberMaster
//    SET
//    Password = @ConfirmPassword
//    ,linktoMemberMasterIdUpdatedBy = @linktoMemberMasterIdUpdatedBy
//    ,UpdateDateTime = @UpdateDateTime
//    WHERE
//            MemberMasterId = @MemberMasterId
//            AND Password = @Password
//
//    IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterPersonalDetail_Update]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymMemberMasterPersonalDetail_Update]
//    @MemberMasterId int
//    ,@LastLoginDateTime datetime
//    ,@Qualification varchar(50)
//    ,@BloodGroup varchar(3) = NULL
//    ,@Profession varchar(150) = NULL
//    ,@AnniversaryDate date = NULL
//    ,@linktoMemberMasterIdUpdatedBy int = NULL
//    ,@UpdateDateTime datetime = NULL
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//    SET NOCOUNT OFF
//
//    IF NOT EXISTS(SELECT MemberMasterId FROM mymMemberMaster WHERE MemberMasterId = @MemberMasterId AND IsDeleted = 0)
//    BEGIN
//    SET @Status = -2
//    RETURN
//            END
//    UPDATE mymMemberMaster
//    SET
//    LastLoginDateTime = @LastLoginDateTime
//    ,Qualification = @Qualification
//    ,BloodGroup = @BloodGroup
//    ,Profession = @Profession
//    ,AnniversaryDate = @AnniversaryDate
//    ,linktoMemberMasterIdUpdatedBy = @linktoMemberMasterIdUpdatedBy
//    ,UpdateDateTime = @UpdateDateTime
//    WHERE
//            MemberMasterId = @MemberMasterId
//
//            IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//
//            RETURN
//    END
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterProfession_SelectAll]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMasterProfession_SelectAll]
//
//    AS
//            BEGIN
//
//    SET NOCOUNT ON;
//
//    SELECT DISTINCT
//    Profession
//            FROM
//    mymMemberMaster
//    ORDER BY Profession
//
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberMasterQualification_SelectAll]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberMasterQualification_SelectAll]
//
//    AS
//            BEGIN
//
//    SET NOCOUNT ON;
//
//    SELECT DISTINCT
//    Qualification
//            FROM
//    mymMemberMaster
//    ORDER BY Qualification
//
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberRelativesTran_Insert]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymMemberRelativesTran_Insert]
//    @MemberRelativesTranId int OUTPUT
//    ,@linktoMemberMasterId int
//    ,@RelativeName varchar(150)
//    ,@ImageName varchar(100) = NULL
//    ,@Gender varchar(6)
//    ,@BirthDate date = NULL
//    ,@Relation varchar(10)
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//    SET NOCOUNT OFF
//
//    IF EXISTS(SELECT MemberRelativesTranId FROM mymMemberRelativesTran WHERE MemberRelativesTranId = @MemberRelativesTranId)
//    BEGIN
//    SELECT @MemberRelativesTranId = MemberRelativesTranId FROM mymMemberRelativesTran WHERE MemberRelativesTranId = @MemberRelativesTranId
//    SET @Status = -2
//    RETURN
//            END
//
//    INSERT INTO mymMemberRelativesTran
//            (
//                    linktoMemberMasterId
//                    ,RelativeName
//                    ,Gender
//                    ,ImageName
//                    ,BirthDate
//                    ,Relation
//                    )
//    VALUES
//            (
//                    @linktoMemberMasterId
//                    ,@RelativeName
//                    ,@Gender
//                    ,@ImageName
//                    ,@BirthDate
//                    ,@Relation
//                    )
//
//    IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @MemberRelativesTranId = @@IDENTITY
//    SET @Status = 0
//    END
//
//            RETURN
//    END
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberRelativesTran_Update]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberRelativesTran_Update]
//    @MemberRelativesTranId int
//    ,@linktoMemberMasterId int
//    ,@RelativeName varchar(150)
//    ,@ImageName varchar(100) = NULL
//    ,@Gender varchar(6)
//    ,@BirthDate date = NULL
//    ,@Relation varchar(10)
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//    IF EXISTS(SELECT MemberRelativesTranId FROM mymMemberRelativesTran WHERE MemberRelativesTranId = @MemberRelativesTranId AND MemberRelativesTranId != @MemberRelativesTranId)
//    BEGIN
//    SET @Status = -2
//    RETURN
//            END
//    UPDATE mymMemberRelativesTran
//    SET
//    RelativeName = @RelativeName
//    ,ImageName = @ImageName
//    ,Gender = @Gender
//    ,BirthDate = @BirthDate
//    ,Relation = @Relation
//    WHERE
//            MemberRelativesTranId = @MemberRelativesTranId
//            AND linktoMemberMasterId = @linktoMemberMasterId
//
//
//    IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//
//            RETURN
//    END
//
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberRelativesTranbyMemberMasterId_DeleteAll]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymMemberRelativesTranbyMemberMasterId_DeleteAll]
//    @linktoMemberMasterId int
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//    SET NOCOUNT OFF
//
//            DELETE
//    FROM
//            mymMemberRelativesTran
//    WHERE
//            linktoMemberMasterId = @linktoMemberMasterId
//
//            IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @Status = 0
//    END
//
//            RETURN
//    END
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymMemberRelativesTranbyMemberMasterId_SelectAll]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//
//    CREATE PROCEDURE [dbo].[mymMemberRelativesTranbyMemberMasterId_SelectAll]
//    @linktoMemberMasterId int
//            AS
//    BEGIN
//
//    SET NOCOUNT ON;
//
//    SELECT
//    mymMemberRelativesTran.*
//    FROM
//            mymMemberRelativesTran
//    WHERE
//            linktoMemberMasterId = @linktoMemberMasterId
//            END
//
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymNotificationMaster_Insert]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymNotificationMaster_Insert]
//    @NotificationMasterId int OUTPUT
//    ,@NotificationTitle varchar(100)
//    ,@NotificationText varchar(500)
//    ,@NotificationImageName varchar(100) = NULL
//    ,@NotificationDateTime datetime
//    ,@CreateDateTime datetime
//    ,@linktoMemberMasterIdCreatedBy int
//    ,@IsDeleted bit
//
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//    SET NOCOUNT OFF
//
//    IF EXISTS(SELECT NotificationMasterId FROM mymNotificationMaster WHERE NotificationMasterId = @NotificationMasterId AND IsDeleted = 0)
//    BEGIN
//    SELECT @NotificationMasterId = NotificationMasterId FROM mymNotificationMaster WHERE NotificationMasterId = @NotificationMasterId
//    SET @Status = -2
//    RETURN
//            END
//    INSERT INTO mymNotificationMaster
//            (
//                    NotificationTitle
//                    ,NotificationText
//                    ,NotificationImageName
//                    ,NotificationDateTime
//                    ,CreateDateTime
//                    ,linktoMemberMasterIdCreatedBy
//                    ,IsDeleted
//
//                    )
//    VALUES
//            (
//                    @NotificationTitle
//                    ,@NotificationText
//                    ,@NotificationImageName
//                    ,@NotificationDateTime
//                    ,@CreateDateTime
//                    ,@linktoMemberMasterIdCreatedBy
//                    ,@IsDeleted
//
//                    )
//
//    IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @NotificationMasterId = @@IDENTITY
//    SET @Status = 0
//    END
//
//            RETURN
//    END
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymNotificationMaster_Select]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymNotificationMaster_Select]
//    @NotificationMasterId int
//            AS
//    BEGIN
//    SET NOCOUNT ON
//
//            SELECT
//    mymNotificationMaster.*
//    FROM
//            mymNotificationMaster
//    WHERE
//            NotificationMasterId = @NotificationMasterId
//
//            RETURN
//    END
//            GO
//    /****** Object:  StoredProcedure [dbo].[mymNotificationMasterPageWise_SelectAll]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymNotificationMasterPageWise_SelectAll]
//    @linktoMemberMasterId int
//    ,@StartRowIndex int
//    ,@PageSize int
//    ,@TotalRowCount int OUTPUT
//    AS
//            BEGIN
//
//    SET NOCOUNT ON
//
//            SELECT
//    @TotalRowCount = COUNT(*)
//    FROM
//            mymNotificationMaster
//    WHERE
//            IsDeleted = 0
//    AND @linktoMemberMasterId IS NOT NULL
//    AND NotificationMasterId NOT IN (SELECT linktoNotificationMasterId FROM mymNotificationTran WHERE linktoMemberMasterId= @linktoMemberMasterId)
//
//    SELECT * FROM
//            (
//                    SELECT ROW_NUMBER() OVER(ORDER BY NotificationMasterId DESC) AS RowNumber
//    ,mymNotificationMaster.*
//    FROM
//            mymNotificationMaster
//    WHERE
//            IsDeleted = 0
//    AND @linktoMemberMasterId IS NOT NULL
//    AND NotificationMasterId NOT IN (SELECT linktoNotificationMasterId FROM mymNotificationTran WHERE linktoMemberMasterId= @linktoMemberMasterId)
//    )
//    AS TEMP_TABLE
//    WHERE RowNumber BETWEEN @StartRowIndex + 1 AND @StartRowIndex + @PageSize
//
//    RETURN
//            END
//    GO
//    /****** Object:  StoredProcedure [dbo].[mymNotificationTran_Insert]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE PROCEDURE [dbo].[mymNotificationTran_Insert]
//    @NotificationTranId bigint OUTPUT
//    ,@linktoNotificationMasterId int
//    ,@linktoMemberMasterId int
//    ,@ReadDateTime datetime
//    ,@Status smallint OUTPUT
//    AS
//            BEGIN
//    SET NOCOUNT OFF
//
//    IF EXISTS(SELECT NotificationTranId FROM mymNotificationTran WHERE NotificationTranId = @NotificationTranId)
//    BEGIN
//    SELECT @NotificationTranId = NotificationTranId FROM mymNotificationTran WHERE NotificationTranId = @NotificationTranId
//    SET @Status = -2
//    RETURN
//            END
//    INSERT INTO mymNotificationTran
//            (
//                    linktoNotificationMasterId
//                    ,linktoMemberMasterId
//                    ,ReadDateTime
//                    )
//    VALUES
//            (
//                    @linktoNotificationMasterId
//                    ,@linktoMemberMasterId
//                    ,@ReadDateTime
//                    )
//
//    IF @@ERROR <> 0
//    BEGIN
//    SET @Status = -1
//    END
//            ELSE
//    BEGIN
//    SET @NotificationTranId = @@IDENTITY
//    SET @Status = 0
//    END
//
//            RETURN
//    END
//            GO
//    /****** Object:  Table [dbo].[mymAdvertiseMaster]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    SET ANSI_PADDING ON
//            GO
//    CREATE TABLE [dbo].[mymAdvertiseMaster](
//            [AdvertiseMasterId] [int] IDENTITY(1,1) NOT NULL,
//    [AdvertiseText] [varchar](500) NULL,
//            [AdvertiseImageName] [varchar](100) NULL,
//            [WebsiteURL] [varchar](150) NULL,
//            [AdvertisementType] [varchar](10) NOT NULL,
//    [linktoMemberMasterIdCreatedBy] [int] NOT NULL,
//    [CreateDateTime] [datetime] NOT NULL,
//    [linktoMemberMasterIdUpdatedBy] [int] NULL,
//            [UpdateDateTime] [datetime] NULL,
//            [IsEnabled] [bit] NOT NULL,
//    [IsDeleted] [bit] NOT NULL,
//    CONSTRAINT [PK_mymAdvertiseMaster] PRIMARY KEY CLUSTERED
//            (
//                    [AdvertiseMasterId] ASC
//            )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
//            ) ON [PRIMARY]
//
//    GO
//    SET ANSI_PADDING OFF
//            GO
//    /****** Object:  Table [dbo].[mymErrorLog]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    SET ANSI_PADDING ON
//            GO
//    CREATE TABLE [dbo].[mymErrorLog](
//            [ErrorLogId] [smallint] IDENTITY(1,1) NOT NULL,
//    [ErrorDateTime] [datetime] NOT NULL,
//    [ErrorMessage] [varchar](500) NOT NULL,
//    [ErrorStackTrace] [varchar](4000) NOT NULL,
//    [LastErrorDateTime] [datetime] NULL,
//            [ErrorCount] [smallint] NOT NULL,
//    CONSTRAINT [PK_mymErrorLog] PRIMARY KEY CLUSTERED
//            (
//                    [ErrorLogId] ASC
//            )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
//            ) ON [PRIMARY]
//
//    GO
//    SET ANSI_PADDING OFF
//            GO
//    /****** Object:  Table [dbo].[mymMemberMaster]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    SET ANSI_PADDING ON
//            GO
//    CREATE TABLE [dbo].[mymMemberMaster](
//            [MemberMasterId] [int] IDENTITY(1,1) NOT NULL,
//    [MemberName] [varchar](150) NOT NULL,
//    [ImageName] [varchar](100) NULL,
//            [Phone1] [varchar](15) NOT NULL,
//    [Phone2] [varchar](15) NULL,
//            [Email] [varchar](80) NULL,
//            [Password] [varchar](25) NOT NULL,
//    [MemberType] [varchar](10) NOT NULL,
//    [LastLoginDateTime] [datetime] NOT NULL,
//    [Gender] [varchar](6) NOT NULL,
//    [Qualification] [varchar](50) NULL,
//            [BloodGroup] [varchar](3) NULL,
//            [Profession] [varchar](150) NULL,
//            [BirthDate] [date] NULL,
//            [AnniversaryDate] [date] NULL,
//            [HomeCountry] [varchar](50) NULL,
//            [HomeState] [varchar](50) NULL,
//            [HomeCity] [varchar](50) NULL,
//            [HomeArea] [varchar](50) NULL,
//            [HomeNumberStreet] [varchar](100) NULL,
//            [HomeNearBy] [varchar](50) NULL,
//            [HomeZipCode] [varchar](10) NULL,
//            [HomePhone] [varchar](80) NULL,
//            [OfficeCountry] [varchar](50) NULL,
//            [OfficeState] [varchar](50) NULL,
//            [OfficeCity] [varchar](50) NULL,
//            [OfficeArea] [varchar](50) NULL,
//            [OfficeNumberStreet] [varchar](100) NULL,
//            [OfficeNearBy] [varchar](50) NULL,
//            [OfficeZipCode] [varchar](10) NULL,
//            [OfficePhone] [varchar](80) NULL,
//            [IsApproved] [bit] NULL,
//            [IsAdminNotificationSent] [bit] NOT NULL,
//    [IsMemberNotificationSent] [bit] NOT NULL,
//    [FCMToken] [varchar](250) NULL,
//            [linktoMemberMasterIdApprovedBy] [int] NULL,
//            [ApprovedDateTime] [datetime] NULL,
//            [linktoMemberMasterIdUpdatedBy] [int] NULL,
//            [UpdateDateTime] [datetime] NULL,
//            [IsDeleted] [bit] NOT NULL,
//    CONSTRAINT [PK_mymMemberMaster] PRIMARY KEY CLUSTERED
//            (
//                    [MemberMasterId] ASC
//            )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
//            ) ON [PRIMARY]
//
//    GO
//    SET ANSI_PADDING OFF
//            GO
//    /****** Object:  Table [dbo].[mymMemberRelativesTran]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    SET ANSI_PADDING ON
//            GO
//    CREATE TABLE [dbo].[mymMemberRelativesTran](
//            [MemberRelativesTranId] [int] IDENTITY(1,1) NOT NULL,
//    [linktoMemberMasterId] [int] NOT NULL,
//    [RelativeName] [varchar](150) NOT NULL,
//    [ImageName] [varchar](100) NULL,
//            [Gender] [varchar](6) NOT NULL,
//    [BirthDate] [date] NULL,
//            [Relation] [varchar](10) NOT NULL,
//    CONSTRAINT [PK_mymMemberRelativesTran] PRIMARY KEY CLUSTERED
//            (
//                    [MemberRelativesTranId] ASC
//            )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
//            ) ON [PRIMARY]
//
//    GO
//    SET ANSI_PADDING OFF
//            GO
//    /****** Object:  Table [dbo].[mymNotificationMaster]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    SET ANSI_PADDING ON
//            GO
//    CREATE TABLE [dbo].[mymNotificationMaster](
//            [NotificationMasterId] [int] IDENTITY(1,1) NOT NULL,
//    [NotificationTitle] [varchar](100) NOT NULL,
//    [NotificationText] [varchar](500) NOT NULL,
//    [NotificationImageName] [varchar](100) NULL,
//            [NotificationDateTime] [datetime] NOT NULL,
//    [CreateDateTime] [datetime] NOT NULL,
//    [linktoMemberMasterIdCreatedBy] [int] NOT NULL,
//    [UpdateDateTime] [datetime] NULL,
//            [linktoMemberMasterIdUpdatedBy] [int] NULL,
//            [IsDeleted] [bit] NOT NULL,
//    CONSTRAINT [PK_mymNotificationMaster] PRIMARY KEY CLUSTERED
//            (
//                    [NotificationMasterId] ASC
//            )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
//            ) ON [PRIMARY]
//
//    GO
//    SET ANSI_PADDING OFF
//            GO
//    /****** Object:  Table [dbo].[mymNotificationTran]    Script Date: 30/12/2016 1:04:01 PM ******/
//    SET ANSI_NULLS ON
//            GO
//    SET QUOTED_IDENTIFIER ON
//            GO
//    CREATE TABLE [dbo].[mymNotificationTran](
//            [NotificationTranId] [bigint] IDENTITY(1,1) NOT NULL,
//    [linktoNotificationMasterId] [int] NOT NULL,
//    [linktoMemberMasterId] [int] NOT NULL,
//    [ReadDateTime] [datetime] NOT NULL,
//    CONSTRAINT [PK_mymNotificationTran] PRIMARY KEY CLUSTERED
//            (
//                    [NotificationTranId] ASC
//            )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
//            ) ON [PRIMARY]
//
//    GO
//    ALTER TABLE [dbo].[mymAdvertiseMaster] ADD  CONSTRAINT [DF_mymAdvertiseMaster_CreateDateTime]  DEFAULT (getdate()) FOR [CreateDateTime]
//    GO
//    ALTER TABLE [dbo].[mymAdvertiseMaster] ADD  CONSTRAINT [DF_mymAdvertiseMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
//    GO
//    ALTER TABLE [dbo].[mymErrorLog] ADD  CONSTRAINT [DF_mymErrorLog_ErrorCount]  DEFAULT ((0)) FOR [ErrorCount]
//    GO
//    ALTER TABLE [dbo].[mymMemberMaster] ADD  CONSTRAINT [DF_mymMemberMaster_IsAdminNotificationSent]  DEFAULT ((0)) FOR [IsAdminNotificationSent]
//    GO
//    ALTER TABLE [dbo].[mymMemberMaster] ADD  CONSTRAINT [DF_mymMemberMaster_IsMemberNotificationSent]  DEFAULT ((0)) FOR [IsMemberNotificationSent]
//    GO
//    ALTER TABLE [dbo].[mymMemberMaster] ADD  CONSTRAINT [DF_mymMemberMaster_CreateDateTime]  DEFAULT (getdate()) FOR [ApprovedDateTime]
//    GO
//    ALTER TABLE [dbo].[mymMemberMaster] ADD  CONSTRAINT [DF_mymMemberMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
//    GO
//    ALTER TABLE [dbo].[mymNotificationMaster] ADD  CONSTRAINT [DF_mymNotificationMaster_CreateDateTime]  DEFAULT (getdate()) FOR [CreateDateTime]
//    GO
//    ALTER TABLE [dbo].[mymNotificationMaster] ADD  CONSTRAINT [DF_mymNotificationMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
//    GO
//    USE [master]
//    GO
//    ALTER DATABASE [abMYM] SET  READ_WRITE
//    GO
//
//}

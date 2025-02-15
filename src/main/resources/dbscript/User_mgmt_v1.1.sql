USE [UserManagement]
GO
/****** Object:  Schema [ALERT]    Script Date: 12/12/2023 9:27:52 AM ******/
CREATE SCHEMA [ALERT]
GO
/****** Object:  Schema [LICENSE]    Script Date: 12/12/2023 9:27:52 AM ******/
CREATE SCHEMA [LICENSE]
GO
/****** Object:  Schema [ROLE_MANAGEMENT]    Script Date: 12/12/2023 9:27:52 AM ******/
CREATE SCHEMA [ROLE_MANAGEMENT]
GO
/****** Object:  Schema [USER]    Script Date: 12/12/2023 9:27:52 AM ******/
CREATE SCHEMA [USER]
GO
/****** Object:  Table [ALERT].[PCS_ALERT_CONFIGURATION]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [ALERT].[PCS_ALERT_CONFIGURATION](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[access_page_ID] [int] NOT NULL,
	[event_RID] [int] NOT NULL,
	[org_ID] [nvarchar](72) NOT NULL,
	[branch_ID] [nvarchar](72) NOT NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK_PCS_ALERT_CONFIGURATION] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [ALERT].[PCS_ALERT_EMAIL]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [ALERT].[PCS_ALERT_EMAIL](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[PCS_alert_config_ID] [int] NOT NULL,
	[email] [nvarchar](1000) NOT NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK_PCS_ALERT_EMAIL] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [ALERT].[PCS_ALERT_SMS]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [ALERT].[PCS_ALERT_SMS](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[PCS_alert_config_ID] [int] NOT NULL,
	[mobile] [nvarchar](100) NOT NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](7) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK_PCS_ALERT_SMS] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [ALERT].[PMIS_ALERT_CONFIGURATION]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [ALERT].[PMIS_ALERT_CONFIGURATION](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[access_page_ID] [int] NOT NULL,
	[event_RID] [int] NOT NULL,
	[stakeholdertype_ID] [int] NOT NULL,
	[email] [bit] NOT NULL,
	[mobile] [bit] NOT NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK_PMIS_ALERT_CONFIGURATION] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [ALERT].[TEMPLATE_MASTER]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [ALERT].[TEMPLATE_MASTER](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[access_page_ID] [int] NOT NULL,
	[event_RID] [int] NOT NULL,
	[PCS_email_header_eng] [nvarchar](1000) NOT NULL,
	[PCS_email_header_arb] [nvarchar](1000) NOT NULL,
	[PCS_email_body_eng] [nvarchar](max) NOT NULL,
	[PCS_email_body_arb] [nvarchar](max) NOT NULL,
	[PCS_sms_eng] [nvarchar](1000) NOT NULL,
	[PCS_sms_arb] [nvarchar](1000) NOT NULL,
	[PMIS_email_header_eng] [nvarchar](1000) NOT NULL,
	[PMIS_email_header_arb] [nvarchar](1000) NOT NULL,
	[PMIS_email_body_eng] [nvarchar](max) NOT NULL,
	[PMIS_email_body_arb] [nvarchar](max) NOT NULL,
	[PMIS_sms_eng] [nvarchar](1000) NOT NULL,
	[PMIS_sms_arb] [nvarchar](1000) NOT NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK_TEMPLATE_MASTER] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [LICENSE].[LICENSE_APPLICATION]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [LICENSE].[LICENSE_APPLICATION](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[user_org_ID] [int] NOT NULL,
	[CRN] [bigint] NOT NULL,
	[license_type_RID] [int] NOT NULL,
	[investor_type_RID] [int] NOT NULL,
	[commercial_registration] [bigint] NOT NULL,
	[license_number] [nvarchar](30) NULL,
	[license_expiry_date] [date] NULL,
	[approval_status_RID] [int] NOT NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [LICENSE].[LICENSE_APPLICATION_APPROVAL]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [LICENSE].[LICENSE_APPLICATION_APPROVAL](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[license_application_ID] [int] NOT NULL,
	[user_RID] [nvarchar](72) NOT NULL,
	[approval_status_RID] [int] NOT NULL,
	[remark] [nvarchar](600) NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [LICENSE].[PORT_PERMIT_APPLICATION]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [LICENSE].[PORT_PERMIT_APPLICATION](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[user_org_ID] [int] NOT NULL,
	[CRN] [bigint] NOT NULL,
	[port_RID] [int] NOT NULL,
	[EUNN] [bigint] NOT NULL,
	[establishment_name] [nvarchar](70) NOT NULL,
	[establishment_type] [nvarchar](40) NOT NULL,
	[establishment_activity] [nvarchar](40) NOT NULL,
	[establishment_status] [nvarchar](20) NOT NULL,
	[establishment_issue_date] [date] NOT NULL,
	[establishment_expiry_date] [date] NOT NULL,
	[establishment_city] [nvarchar](70) NOT NULL,
	[address] [nvarchar](400) NOT NULL,
	[mailbox] [nvarchar](400) NOT NULL,
	[manager_name] [nvarchar](70) NOT NULL,
	[manager_nationality] [nvarchar](70) NOT NULL,
	[list_of_partners] [nvarchar](70) NOT NULL,
	[record_type] [nvarchar](20) NOT NULL,
	[bank_guarantee_filepath] [nvarchar](600) NOT NULL,
	[approval_status_RID] [int] NOT NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK__PORT_PER__3213E83F6E67E30D] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [LICENSE].[PORT_PERMIT_APPLICATION_APPROVAL]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [LICENSE].[PORT_PERMIT_APPLICATION_APPROVAL](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[port_permit_application_ID] [int] NOT NULL,
	[user_RID] [nvarchar](72) NOT NULL,
	[approval_status_RID] [int] NOT NULL,
	[remark] [nvarchar](600) NULL,
	[filepath] [nvarchar](400) NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK__PORT_PER__3213E83F76A4B6F2] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [ROLE_MANAGEMENT].[ACCESS_HEADER]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [ROLE_MANAGEMENT].[ACCESS_HEADER](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[header] [nvarchar](200) NOT NULL,
	[header_description] [nvarchar](200) NULL,
	[header_icon_path] [nvarchar](510) NULL,
	[head_sequence_no] [int] NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK__ACEESS_H__3213E83F3EFE83EA] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [ROLE_MANAGEMENT].[ACCESS_MODULE]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [ROLE_MANAGEMENT].[ACCESS_MODULE](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[access_header_ID] [int] NOT NULL,
	[module] [nvarchar](200) NOT NULL,
	[module_description] [nvarchar](200) NULL,
	[module_icon_path] [nvarchar](510) NULL,
	[module_route] [nvarchar](510) NOT NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK__ACEESS_M__DC50F6D82F0B28A9] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [ROLE_MANAGEMENT].[ACCESS_PAGE]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [ROLE_MANAGEMENT].[ACCESS_PAGE](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[access_module_ID] [int] NOT NULL,
	[app_id] [int] NULL,
	[page] [nvarchar](200) NOT NULL,
	[page_code] [nvarchar](200) NULL,
	[page_description] [nvarchar](200) NULL,
	[page_icon_path] [nvarchar](510) NULL,
	[page_route] [nvarchar](510) NOT NULL,
	[sequence] [int] NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK__ACEESS_P__3213E83FD643D589] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [ROLE_MANAGEMENT].[MST_STAKEHOLDERTYPE]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [ROLE_MANAGEMENT].[MST_STAKEHOLDERTYPE](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[role_code] [nvarchar](200) NOT NULL,
	[role_name_english] [nvarchar](200) NOT NULL,
	[role_name_arabic] [nvarchar](200) NOT NULL,
	[role_description_english] [nvarchar](200) NOT NULL,
	[role_description_arabic] [nvarchar](200) NOT NULL,
	[roll_type] [nvarchar](100) NULL,
	[is_licence_required] [bit] NULL,
	[can_be_assigned] [bit] NULL,
	[app_id] [int] NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK_MST_STAKEHOLDERTYPE] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [ROLE_MANAGEMENT].[STAKEHOLDERTYPE_ACCESS]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [ROLE_MANAGEMENT].[STAKEHOLDERTYPE_ACCESS](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[stakeholder_category_RID] [int] NOT NULL,
	[stakeholdertype_ID] [int] NULL,
	[accesspage_ID] [int] NOT NULL,
	[access_view] [bit] NOT NULL,
	[access_create] [bit] NOT NULL,
	[access_edit] [bit] NOT NULL,
	[access_delete] [bit] NOT NULL,
	[access_print] [bit] NOT NULL,
	[access_approve_reject] [bit] NOT NULL,
	[stakeholdertype_subrole_ID] [int] NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK__SUBROLE___3213E83FF8A0DD9F] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [USER].[USER_BRANCH]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [USER].[USER_BRANCH](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[branch_ID] [nvarchar](72) NULL,
	[org_ID] [int] NULL,
	[branch_name] [nvarchar](140) NULL,
	[location_RID] [int] NOT NULL,
	[port_RID] [int] NOT NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK__USER_BRA__3213E83F12B9DA69] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [USER].[USER_ORGANIZATION]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [USER].[USER_ORGANIZATION](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[CRN] [bigint] NULL,
	[org_ID] [nvarchar](72) NULL,
	[org_name] [nvarchar](140) NULL,
	[is_active] [bit] NOT NULL,
	[registration_through_RID] [int] NULL,
	[registration_type_RID] [int] NULL,
	[document_type_RID] [int] NULL,
	[document_no] [nvarchar](40) NULL,
	[national_id] [nvarchar](20) NULL,
	[national_id_expiry_date] [datetime2](0) NULL,
	[unit_no] [nvarchar](20) NULL,
	[zip_code] [bigint] NULL,
	[building_no] [nvarchar](20) NULL,
	[street] [nvarchar](40) NULL,
	[district_RID] [nvarchar](70) NULL,
	[address] [nvarchar](400) NULL,
	[additional_mobile] [nvarchar](100) NULL,
	[additional_email] [nvarchar](70) NULL,
	[approval_status_RID] [int] NULL,
	[licence_no] [nvarchar](30) NULL,
	[custom_no] [nvarchar](510) NULL,
	[stakeholdertype_RID] [int] NOT NULL,
	[city_RID] [int] NOT NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[user_id] [nvarchar](144) NULL,
	[email_id] [nvarchar](70) NULL,
	[mobile_number] [bigint] NULL,
 CONSTRAINT [PK__USER_ORG__3213E83F12B9DA59] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [USER].[USER_ORGANIZATION_APPROVAL]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [USER].[USER_ORGANIZATION_APPROVAL](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[user_organization_ID] [int] NOT NULL,
	[action_by_id] [nvarchar](72) NOT NULL,
	[approval_status_RID] [int] NOT NULL,
	[remark] [nvarchar](600) NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [USER].[USER_REGISTRATION]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [USER].[USER_REGISTRATION](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[app_id] [int] NULL,
	[employee_code] [nvarchar](30) NOT NULL,
	[employee_name] [nvarchar](70) NOT NULL,
	[joining_date] [date] NULL,
	[department_RID] [int] NOT NULL,
	[designation_RID] [int] NOT NULL,
	[port_RID] [int] NOT NULL,
	[default_group_type_RID] [int] NOT NULL,
	[email] [nvarchar](70) NOT NULL,
	[phone_no] [bigint] NOT NULL,
	[IAM_user_ID] [nvarchar](36) NOT NULL,
	[org_ID] [nvarchar](72) NULL,
	[branch_ID] [nvarchar](72) NULL,
	[user_type] [nvarchar](20) NULL,
	[user_id] [nvarchar](144) NULL,
	[port_user_no] [nvarchar](510) NULL,
	[profile_image_path] [nvarchar](600) NULL,
	[stakeholdertype_ID] [int] NULL,
	[stakeholdertype_subrole_ID] [int] NULL,
	[national_ID] [nvarchar](20) NULL,
	[unit_number] [nvarchar](20) NULL,
	[zip_code] [int] NULL,
	[building_number] [nvarchar](20) NULL,
	[street] [nvarchar](40) NULL,
	[district] [nvarchar](40) NULL,
	[city] [int] NULL,
	[address] [nvarchar](70) NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
	[rep_no] [nvarchar](30) NULL,
 CONSTRAINT [PK__USER_REG__3213E83F12B9DA59] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [USER].[USER_ROLE]    Script Date: 12/12/2023 9:27:52 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [USER].[USER_ROLE](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[user_registration_id] [int] NOT NULL,
	[stakeholder_type_id] [int] NOT NULL,
	[stakeholdertype_subrole_id] [int] NOT NULL,
	[created_by] [nvarchar](72) NULL,
	[created_date] [datetime2](0) NULL,
	[updated_by] [nvarchar](72) NULL,
	[updated_date] [datetime2](0) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [PK__USER_ROL__3213E83F8A5AEAE1] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [ALERT].[PCS_ALERT_CONFIGURATION]  WITH CHECK ADD  CONSTRAINT [fk_pcsalertconfiguration_accesspage] FOREIGN KEY([access_page_ID])
REFERENCES [ROLE_MANAGEMENT].[ACCESS_PAGE] ([id])
GO
ALTER TABLE [ALERT].[PCS_ALERT_CONFIGURATION] CHECK CONSTRAINT [fk_pcsalertconfiguration_accesspage]
GO
ALTER TABLE [ALERT].[PCS_ALERT_EMAIL]  WITH CHECK ADD  CONSTRAINT [fk_pcsalertmail_pcsalertconfiguration] FOREIGN KEY([PCS_alert_config_ID])
REFERENCES [ALERT].[PCS_ALERT_CONFIGURATION] ([id])
GO
ALTER TABLE [ALERT].[PCS_ALERT_EMAIL] CHECK CONSTRAINT [fk_pcsalertmail_pcsalertconfiguration]
GO
ALTER TABLE [ALERT].[PCS_ALERT_SMS]  WITH CHECK ADD  CONSTRAINT [fk_pcsalertsms_pcsalertconfiguration] FOREIGN KEY([PCS_alert_config_ID])
REFERENCES [ALERT].[PCS_ALERT_CONFIGURATION] ([id])
GO
ALTER TABLE [ALERT].[PCS_ALERT_SMS] CHECK CONSTRAINT [fk_pcsalertsms_pcsalertconfiguration]
GO
ALTER TABLE [ALERT].[PMIS_ALERT_CONFIGURATION]  WITH CHECK ADD  CONSTRAINT [fk_pmisalertconfiguration_accesspage] FOREIGN KEY([access_page_ID])
REFERENCES [ROLE_MANAGEMENT].[ACCESS_PAGE] ([id])
GO
ALTER TABLE [ALERT].[PMIS_ALERT_CONFIGURATION] CHECK CONSTRAINT [fk_pmisalertconfiguration_accesspage]
GO
ALTER TABLE [ALERT].[TEMPLATE_MASTER]  WITH CHECK ADD  CONSTRAINT [fk_templatemaster_accesspage] FOREIGN KEY([access_page_ID])
REFERENCES [ROLE_MANAGEMENT].[ACCESS_PAGE] ([id])
GO
ALTER TABLE [ALERT].[TEMPLATE_MASTER] CHECK CONSTRAINT [fk_templatemaster_accesspage]
GO
ALTER TABLE [ROLE_MANAGEMENT].[ACCESS_MODULE]  WITH CHECK ADD  CONSTRAINT [fk_accessmodule_accessheader] FOREIGN KEY([access_header_ID])
REFERENCES [ROLE_MANAGEMENT].[ACCESS_HEADER] ([id])
GO
ALTER TABLE [ROLE_MANAGEMENT].[ACCESS_MODULE] CHECK CONSTRAINT [fk_accessmodule_accessheader]
GO
ALTER TABLE [ROLE_MANAGEMENT].[ACCESS_PAGE]  WITH CHECK ADD  CONSTRAINT [fk_accesspage_accesmodule] FOREIGN KEY([access_module_ID])
REFERENCES [ROLE_MANAGEMENT].[ACCESS_MODULE] ([id])
GO
ALTER TABLE [ROLE_MANAGEMENT].[ACCESS_PAGE] CHECK CONSTRAINT [fk_accesspage_accesmodule]
GO
ALTER TABLE [USER].[USER_ORGANIZATION_APPROVAL]  WITH CHECK ADD  CONSTRAINT [fk_userorganizationapproval_userorganization] FOREIGN KEY([user_organization_ID])
REFERENCES [USER].[USER_ORGANIZATION] ([id])
GO
ALTER TABLE [USER].[USER_ORGANIZATION_APPROVAL] CHECK CONSTRAINT [fk_userorganizationapproval_userorganization]
GO

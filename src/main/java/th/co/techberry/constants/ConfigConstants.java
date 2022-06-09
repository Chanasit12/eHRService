package th.co.techberry.constants;

public class ConfigConstants {
	// Database
	public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
//	public static final String DB_HOST = "192.168.1.40";
//	public static final String DB_HOST = "localhost";
	public static final String DB_HOST = "103.2.114.226";
//	public static final String DB_HOST = "103.2.114.70";
//	public static final String DB_HOST = "54.169.214.132";
	public static final String DB_NAME = "e-hr";
	public static final String DB_USER = "root";
	public static final String DB_PASS = "";
	public static final String DAY_OFF_NAME = "DayOff";
	public static final String ROLE_HR_NAME = "Hr";
	// response structure
	public static final String RESPONSE_KEY_SUCCESS = "success";
	public static final String RESPONSE_KEY_MESSAGE = "message";
	public static final String RESPONSE_KEY_ERROR_MESSAGE = "errorMessage";
	public static final String RESPONSE_KEY_CODE = "code";
	public static final String DELETE_COMPANY_ERROR = "There Still Employee In This Company";
	public static final String DELETE_MEETING_ROOM_ERROR = "There Still Meeting Room Booking Use This Meeting Room";
	// Mail
	public static final String MAIL_SMTP_HOST = "smtp.techberry.co.th";
	public static final String MAIL_SMTP_PORT = "25";
	public static final String MAIL_SMTP_SENDER_USER = "ehr_app@techberry.co.th";
	public static final String MAIL_SMTP_SENDER_PASS = "ehr_appP@ssw0rd";
	// Message
	public static final String ID_NOT_FOUND = "Id not found.";
	public static final String PLEASE_INPUT_REQUIRED_FIELD = "Please input required field.";
	public static final String USERNAME_IS_INCORRECT = "Username is incorrect.";
	public static final String USERNAME_NOT_FOUND = "Username not found.";
	public static final String PASSWORD_NOT_FOUND = "Password is incorrect.";
	public static final String LOGIN_INACTIVE_USER = "Sorry, your account is inactive and may not login.";
	public static final String WE_COULD_NOT_FIND = "We couldn't find what you were looking for.";
	public static final String PLEASE_CONTACT_ADMIN = "Service error, Please contact admin.";
	public static final String SENT_TO_EMAIL_SUCESS = "Sent to E-mail success.";
	public static final String SENT_TO_EMAIL_FAILED = "Sent to E-mail Failed, Please contact admin.";
	public static final String PASSWORD_RESET_FAILED = "Password reset failed, Please contact admin.";
	public static final String MAIL_SUBJECT_RESET_PASSWORD = "Reset password for eHR system";
	public static final String MAIL_SUBJECT_EMPLOYEE_REQUEST = "Employee request leave in eHR system";
	public static final String MAIL_SUBJECT_ADD_EMPLOYEE = "Welcome To TechBerry";
	public static final String REQUESTED = "Requested";
	public static final String LEAVE_REQUEST_APPROVED_BY_CHIEF = "Approved by chief";
	public static final String LEAVE_REQUEST_DECLINED_BY_CHIEF = "Declined by chief";
//	public static final String LEAVE_REQUEST_APPROVED_BY_MANAGER = "Approved by manager";
//	public static final String LEAVE_REQUEST_DECLINED_BY_MANAGER = "Declined by manager";
	public static final String APPROVED_BY = "Approved by";
	public static final String DECLINED_BY = "Declined by";
	public static final String APPROVED_BY_APPROVER = "Approved by approver";
	public static final String DECLINED_BY_APPROVER = "Declined by approver";
	public static final String CANCELLATION_REQUEST = "Cancellation Request";
	public static final String CANCELLATION = "Cancellation";
	public static final String APPROVED_CANCELLATION = "Approved cancellation";
	public static final String DECLINED_CANCELLATION = "Declined cancellation";
	public static final String MESSAGE_IN_EMAIL_EXPENSE_REQUEST_MGMT = "There is an employee submitting a request to withdraw expense.";
	public static final String MESSAGE_IN_EMAIL_EXPENSE_REQUEST = "You request has already respond.";
	public static final String MESSAGE_IN_EMAIL_DOCUMENT_REQUEST_MGMT = "There is an employee sending a document request.";
	public static final String MESSAGE_IN_EMAIL_DOCUMENT_REQUEST = "You request has already respond.";
	public static final String SUBJECT_EXPENSE_REQUEST_MAIL = "There is a Expense Request for approval";
	public static final String SUBJECT_DOCUMENT_REQUEST_MAIL = "There is a Document Request";
//	public static final String PATH_MAIL_TEMPLATE = "E:/Mail/";
//	public static final String PATH_MAIL_TEMPLATE = "D:/Mail";
	public static final String PATH_MAIL_TEMPLATE = "/usr/src/app/ds/mails";
	public static final String MAIL_TEMPLATE_FORGOT_PASSWORD = "Mail.ftl";
	public static final String MAIL_TEMPLATE_ADD_EMPLOYEE = "Adduser.ftl";
	public static final String MAIL_TEMPLATE_ADD_MEETING = "Meeting.ftl";
	public static final String MAIL_TEMPLATE_EXPENSE_REQUEST = "ExpenseRequest.ftl";
	public static final String MAIL_TEMPLATE_DOCUMENT_REQUEST = "DocumentRequest.ftl";
	public static final String MAIL_TEMPLATE_DOCUMENT_REQUEST_MGMT = "DocumentRequest_Mgmt.ftl";
	// generate tokenKey
	public static final String SECRET_KEY = "AdxweqaskdlkjsgioujpDOPIUSUDioashodihsidlasd213xz";
	public static final String ENCRYPT_KEY = "d6b7e879de929122";
}

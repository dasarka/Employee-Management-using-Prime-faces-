package emp.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import emp.model.ApprisalBean;
import emp.model.ProjectBean;
import emp.model.Timecard;
import emp.model.UsersBean;
import emp.service.HRService;
import emp.serviceImpl.HRServiceImpl;
import emp.ui.view.TimecardUI;

public class HRController {
	List<Timecard> profileTimecard = new ArrayList<Timecard>();
	List<Timecard> newTimecard = new ArrayList<Timecard>();
	List<Timecard> hrTimecard = new ArrayList<Timecard>();
	List<UsersBean> usersList = new ArrayList<UsersBean>();
	
	List<ApprisalBean> apprisalList=new ArrayList<ApprisalBean>();
	ApprisalBean apprisalBean=new ApprisalBean();
	private int overallRating=0;
	private boolean apprisalContent;
	
	public boolean isApprisalContent() {
		return apprisalContent;
	}

	public void setApprisalContent(boolean apprisalContent) {
		this.apprisalContent = apprisalContent;
	}

	public int getOverallRating() {
		return overallRating;
	}

	public void setOverallRating(int overallRating) {
		this.overallRating = overallRating;
	}
	
	public List<ApprisalBean> getApprisalList() {
		return apprisalList;
	}

	public void setApprisalList(List<ApprisalBean> apprisalList) {
		this.apprisalList = apprisalList;
	}

	public ApprisalBean getApprisalBean() {
		return apprisalBean;
	}

	public void setApprisalBean(ApprisalBean apprisalBean) {
		this.apprisalBean = apprisalBean;
	}
	private int selectedUsers;
	
	
	
	public int getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(int selectedUsers) {
		this.selectedUsers = selectedUsers;
	}
	TimecardUI timecardUI = new TimecardUI();

	public TimecardUI getTimecardUI() {
		return timecardUI;
	}

	public void setTimecardUI(TimecardUI timecardUI) {
		this.timecardUI = timecardUI;
	}

	public List<UsersBean> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<UsersBean> usersList) {
		this.usersList = usersList;
	}

	public List<Timecard> getProfileTimecard() {
		return profileTimecard;
	}

	public void setProfileTimecard(List<Timecard> profileTimecard) {
		this.profileTimecard = profileTimecard;
	}

	public List<Timecard> getNewTimecard() {
		return newTimecard;
	}

	public void setNewTimecard(List<Timecard> newTimecard) {
		this.newTimecard = newTimecard;
	}

	public List<Timecard> getHrTimecard() {
		return hrTimecard;
	}

	public void setHrTimecard(List<Timecard> hrTimecard) {
		this.hrTimecard = hrTimecard;
	}

	UsersBean SessionData() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		UsersBean usersBean = (UsersBean) session.getAttribute("usersBean");
		System.out.println("Session usersbean " + usersBean);
		return usersBean;
	}

	public String getLoadNewTimeCard() {
		System.out.println("getLoadNewTimeCard");
		newTimecard.clear();
		Date date=new Date();
		SimpleDateFormat simpleFormat=new SimpleDateFormat("dd-MMM-yyyy");
		String today=simpleFormat.format(date);
		System.out.println("today "+today);
		String year=today.split("-")[2];
		System.out.println("year "+year);
		String month=today.split("-")[1];
		timecardUI.setSelectedMonth(month);
		timecardUI.setSelectedYear(year);
		HRService hrService = new HRServiceImpl();
		UsersBean userData = SessionData();
		try {
			profileTimecard=hrService.ViewTimeCardService(month, year, userData.getUserId());
			if(profileTimecard.size()>0){
				timecardUI.setDisableTimeCard(true);
				FacesContext
				.getCurrentInstance()
				.addMessage(
						"empDashboard:dashTab:timesheet:timecardMsg",
						new FacesMessage(
								FacesMessage.SEVERITY_INFO,
								"you have already sumbitted your timesheet for current month",
								null));
			}else{
				timecardUI.setDisableTimeCard(false);
				FacesContext
				.getCurrentInstance()
				.addMessage(
						"empDashboard:dashTab:timesheet:timecardMsg",
						new FacesMessage(
								FacesMessage.SEVERITY_INFO,
								"Please fillup your timesheet as soon as possible",
								null));
			}
			
		} catch (Exception e1) {
			FacesContext
			.getCurrentInstance()
			.addMessage(
					"empDashboard:dashTab:timesheet:timecardMsg",
					new FacesMessage(
							FacesMessage.SEVERITY_FATAL,
							"We have faced some issue, please wait for some time or contact with support team",
							null));
			e1.printStackTrace();
		}
		if (!timecardUI.isDisableTimeCard()) {
			
			Calendar now = Calendar.getInstance();
			int totalDays = now.getActualMaximum(Calendar.DAY_OF_MONTH);
			now.set(Calendar.DAY_OF_MONTH,
					now.getActualMinimum(Calendar.DAY_OF_MONTH));

			String firstDay = String.valueOf(now.getTime()).split(" ")[0];
			int count = 1;
			int dayCount = 1;
			try {
				
				List<ProjectBean> projectList = hrService
						.ProjectListService(userData.getUserId());

				//
				switch (firstDay) {
				case "Mon": {
					newTimecard.add(new Timecard("Week" + (count++),
							new int[] { 0 }, userData.getUserId(), 0, 0, 0, 0,
							0, false, false, false, false, false, projectList));
					dayCount += 4;
					break;
				}
				case "Tue": {
					newTimecard.add(new Timecard("Week" + (count++),
							new int[] { 0 }, userData.getUserId(), 0, 0, 0, 0,
							0, true, false, false, false, false, projectList));
					dayCount += 3;
					break;
				}
				case "Wed": {
					newTimecard.add(new Timecard("Week" + (count++),
							new int[] { 0 }, userData.getUserId(), 0, 0, 0, 0,
							0, true, true, false, false, false, projectList));
					dayCount += 2;
					break;
				}
				case "Thu": {
					newTimecard.add(new Timecard("Week" + (count++),
							new int[] { 0 }, userData.getUserId(), 0, 0, 0, 0,
							0, true, true, true, false, false, projectList));
					dayCount += 1;
					break;
				}
				case "Fri": {
					newTimecard.add(new Timecard("Week" + (count++),
							new int[] { 0 }, userData.getUserId(), 0, 0, 0, 0,
							0, true, true, true, true, false, projectList));
					break;
				}
				case "Sat": {
					newTimecard.add(new Timecard("Week" + (count++),
							new int[] { 0 }, userData.getUserId(), 0, 0, 0, 0,
							0, false, false, false, false, false, projectList));
					dayCount += 6;
					break;
				}
				default: {
					newTimecard.add(new Timecard("Week" + (count++),
							new int[] { 0 }, userData.getUserId(), 0, 0, 0, 0,
							0, false, false, false, false, false, projectList));
					dayCount += 5;
					break;
				}
				}
				System.out.println("dayCount " + dayCount);
				dayCount += 2;
				System.out.println("dayCount " + dayCount);
				while ((dayCount + 4) < totalDays) {
					System.out.println("week " + count);
					newTimecard.add(new Timecard("Week" + (count++),
							new int[] { 0 }, userData.getUserId(), 0, 0, 0, 0,
							0, false, false, false, false, false, projectList));
					dayCount += 7;
					System.out.println("dayCount " + dayCount);
				}
				int lastWeekDays = totalDays - dayCount;
				switch (lastWeekDays) {
				case 5: {
					newTimecard.add(new Timecard("Week" + (count),
							new int[] { 0 }, userData.getUserId(), 0, 0, 0, 0,
							0, false, false, false, false, false, projectList));
					break;
				}
				case 4: {
					newTimecard.add(new Timecard("Week" + (count),
							new int[] { 0 }, userData.getUserId(), 0, 0, 0, 0,
							0, false, false, false, false, true, projectList));
					break;
				}
				case 3: {
					newTimecard.add(new Timecard("Week" + (count),
							new int[] { 0 }, userData.getUserId(), 0, 0, 0, 0,
							0, false, false, false, true, true, projectList));
					break;
				}
				case 2: {
					newTimecard.add(new Timecard("Week" + (count),
							new int[] { 0 }, userData.getUserId(), 0, 0, 0, 0,
							0, false, false, true, true, true, projectList));
					dayCount += 1;
					break;
				}
				case 1: {
					newTimecard.add(new Timecard("Week" + (count),
							new int[] { 0 }, userData.getUserId(), 0, 0, 0, 0,
							0, false, true, true, true, false, projectList));
					break;
				}
				default: {

					break;
				}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				FacesContext
				.getCurrentInstance()
				.addMessage(
						"empDashboard:dashTab:timesheet:timecardMsg",
						new FacesMessage(
								FacesMessage.SEVERITY_FATAL,
								"We have faced some issue, please wait for some time or contact with support team",
								null));
			}
		}
		return null;
	}

	public void FillNewTimeSheet(AjaxBehaviorEvent ev) {
		System.out.println("FillNewTimeSheet ");
		System.out.println("newTimecard " + newTimecard);
		HRService hrService = new HRServiceImpl();
		try {
			hrService.NewTimeCardService(newTimecard);
			timecardUI.setDisableTimeCard(true);
			FacesContext
			.getCurrentInstance()
			.addMessage(
					"empDashboard:dashTab:timesheet:timecardMsg",
					new FacesMessage(
							FacesMessage.SEVERITY_INFO,
							"Your timesheet has successfully submitted and sent to HR for approval",
							null));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext
			.getCurrentInstance()
			.addMessage(
					"empDashboard:dashTab:timesheet:timecardMsg",
					new FacesMessage(
							FacesMessage.SEVERITY_FATAL,
							"We have faced some issue, please wait for some time or contact with support team",
							null));
		}
	}
	public void ViewTimeCard(AjaxBehaviorEvent event) {
		System.out.println("ViewTimeCard ");
		System.out.println("timecardUI " + timecardUI);
		HRService hrService = new HRServiceImpl();
		try {
			profileTimecard=hrService.ViewTimeCardService(timecardUI.getSelectedMonth(), 
					timecardUI.getSelectedYear(), SessionData().getUserId());
			timecardUI.setDisableTimeCard(true);
			System.out.println("profileTimecard "+profileTimecard);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext
			.getCurrentInstance()
			.addMessage(
					"empDashboard:dashTab:timesheet:timecardMsg",
					new FacesMessage(
							FacesMessage.SEVERITY_FATAL,
							"We have faced some issue, please wait for some time or contact with support team",
							null));
		}
	}
	
	public String getLoadUsers(){
		HRService hrService = new HRServiceImpl();
		try {
			usersList=hrService.FetchAllUsersService();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void VerifyTimeCard(AjaxBehaviorEvent event) {
		System.out.println("VerifyTimeCard ");
		Date date=new Date();
		SimpleDateFormat simpleFormat=new SimpleDateFormat("dd-MMM-yyyy");
		String today=simpleFormat.format(date);
		System.out.println("today "+today);
		String year=today.split("-")[2];
		System.out.println("year "+year);
		String month=today.split("-")[1];
		HRService hrService = new HRServiceImpl();
		try {
			hrTimecard=hrService.ViewTimeCardService(month, 
					year, selectedUsers);
			System.out.println("hrTimecard "+hrTimecard);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String UpdateTimeCard() {
		System.out.println("UpdateTimeCard ");
		System.out.println("hrTimecard "+hrTimecard);
		try{
			HRService hrService = new HRServiceImpl();
			hrService.UpdateTimecardService(hrTimecard);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void AddApprisalDataEmployee(AjaxBehaviorEvent ev){
		System.out.println("AddApprisalDataEmployee ");
		try{
			apprisalBean.setUserId(SessionData().getUserId());
			apprisalList.add(apprisalBean);
			apprisalBean=new ApprisalBean();
			FacesContext
			.getCurrentInstance()
			.addMessage(
					"empDashboard:dashTab:apprisalForm:apprisalMsg",
					new FacesMessage(
							FacesMessage.SEVERITY_INFO,
							"Data added successfully",
							null));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext
			.getCurrentInstance()
			.addMessage(
					"empDashboard:dashTab:apprisalForm:apprisalMsg",
					new FacesMessage(
							FacesMessage.SEVERITY_FATAL,
							"We have faced some issue, please wait for some time or contact with support team",
							null));
		}
	}
	public String getApprisalData(){
		System.out.println("getApprisalData ");
		try{
			HRService hrService = new HRServiceImpl();
			apprisalList=hrService.apprisalDataService(SessionData().getUserId());
			System.out.println(apprisalList);
			if(apprisalList.size()>0){
				apprisalContent=true;
				

			}else{
				apprisalContent=false;
			}
			int count=0;
			overallRating=0;
			for (ApprisalBean appBean : apprisalList) {
				overallRating +=appBean.getRating();
				count++;
			}
			if(count>0){
				overallRating =overallRating/count;
				if(overallRating>0){
				FacesContext
				.getCurrentInstance()
				.addMessage(
						"empDashboard:dashTab:apprisalForm:apprisalMsg",
						new FacesMessage(
								FacesMessage.SEVERITY_INFO,
								"you rating is published",
								null));
				}else{
					FacesContext
					.getCurrentInstance()
					.addMessage(
							"empDashboard:dashTab:apprisalForm:apprisalMsg",
							new FacesMessage(
									FacesMessage.SEVERITY_INFO,
									"you already submitted your appraisal for current year, please wait for rating",
									null));
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext
			.getCurrentInstance()
			.addMessage(
					"empDashboard:dashTab:apprisalForm:apprisalMsg",
					new FacesMessage(
							FacesMessage.SEVERITY_FATAL,
							"We have faced some issue, please wait for some time or contact with support team",
							null));
		}

		 return null;
	}
	
	public void SubmitApprisalDataEmployee(AjaxBehaviorEvent ev){
		System.out.println("SubmitApprisalDataEmployee ");
		try{
			HRService hrService = new HRServiceImpl();
			boolean flag=hrService.SubmitApprisalEmployeeService(apprisalList);
			System.out.println(flag);
			if(flag){
			
			int count=0;
			overallRating=0;
			for (ApprisalBean appBean : apprisalList) {
				overallRating +=appBean.getRating();
				count++;
			}
			if(count>0)
				overallRating =overallRating/count;
			
			FacesContext
			.getCurrentInstance()
			.addMessage(
					"empDashboard:dashTab:apprisalForm:apprisalMsg",
					new FacesMessage(
							FacesMessage.SEVERITY_INFO,
							"Your appraisal has submitted successfully",
							null));
			
			}else{
				FacesContext
				.getCurrentInstance()
				.addMessage(
						"empDashboard:dashTab:apprisalForm:apprisalMsg",
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Failed to submit your appraisal, please contact with support team",
								null));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext
			.getCurrentInstance()
			.addMessage(
					"empDashboard:dashTab:apprisalForm:apprisalMsg",
					new FacesMessage(
							FacesMessage.SEVERITY_FATAL,
							"We have faced some issue, please wait for some time or contact with support team",
							null));
		}
	}
	public String UpdateAppraisal() {
		System.out.println("UpdateAppraisal ");
		System.out.println("apprisalList "+apprisalList);
		try{
			HRService hrService = new HRServiceImpl();
			boolean flag=hrService.UpdateAppraisalService(apprisalList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void OpenApprisal(AjaxBehaviorEvent event) {
		System.out.println("OpenApprisal ");
		try{
			HRService hrService = new HRServiceImpl();
			apprisalList=hrService.apprisalDataService(selectedUsers);
			System.out.println(apprisalList);
			
				
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}
}

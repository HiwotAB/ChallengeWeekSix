package com.hiwotab.roboresumeapplication.controller;

import com.hiwotab.roboresumeapplication.model.*;
import com.hiwotab.roboresumeapplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
@Controller
public class MainController {

    @Autowired
    ResumeRepostory resumeRepostory;
    @Autowired
    EduAchievementsRepostory eduAchievementsRepostory;
    @Autowired
    WorkExperiencesRepostory workExperiencesRepostory;
    @Autowired
    SkillsRepostory skillsRepostory;
    @Autowired
    CourseRepository courseRepository;
    /*******************************home Page , default home page and Login***********************************************/

    @RequestMapping("/")
    public String showHomePages(Model model) {
        model.addAttribute("rowNumberP",resumeRepostory.count());
        model.addAttribute("rowNumberE",eduAchievementsRepostory.count());
        model.addAttribute("rowNumberS",skillsRepostory.count());
        model.addAttribute("rowNumberX",workExperiencesRepostory.count());
        model.addAttribute("rowNumberC",courseRepository.count());
        model.addAttribute("allUser", resumeRepostory.findAll());
        model.addAttribute("searchEdu", eduAchievementsRepostory.findAll());
        model.addAttribute("searchExp", workExperiencesRepostory.findAll());
        model.addAttribute("searchSkill", skillsRepostory.findAll());
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/homePage")
    public String showHomePage() {
        return "index";
    }

    /************************************User Info to add user to modify the existing and to delete the existing user information *********************************************************************/
/*This method is used to dispaly a form of person info for a user to enter values*/
    @GetMapping("/addUser")
    public String addUserInfo(Model model) {
        model.addAttribute("newUser", new Resume());
        return "addUser";
    }
    /*This method is used to check the validation for each values has been entered and if it is valid data then it will save it to resume(user )table
    * also store and check  the record of the row number in data base table*/
    @PostMapping("/addUser")
    public String addUserInfo(@Valid @ModelAttribute("newUser") Resume resume, BindingResult bindingResult,Model model){

        if (bindingResult.hasErrors()) {

            return "addUser";
        }
        resumeRepostory.save(resume);
        return "dispUserInfo";
    }
    /*This method is used to modify the existing in forms then update data bas tables according to there modify fields */
    @RequestMapping("/updateUserInfo/{id}")
    public String updateUserInfo(@PathVariable("id") long id, Model model){
        model.addAttribute("newUser", resumeRepostory.findOne(id));
        return "addUser";
    }
    /*This method is used to delete the existing data  records from data base table and dispaly the rest of data which has been there*/
    @RequestMapping("/deleteUserInfo/{id}")
    public String delUserInfo(@PathVariable("id") long id){
        eduAchievementsRepostory.delete(id);
        return "redirect:/listUserInfo";
    }
    /*This method is used to display the existing data  records from data base table*/
    @RequestMapping("/listUserInfo")
    public String listUserInfo(Model model){
        model.addAttribute("searchUser", resumeRepostory.findAll());
        return "listUserInfo";
    }
    /******************Education Information to add education to modify the existing and to delete the existing education information  *******************************************************************/
  /*This method is used to dispaly a form of  education achievement to person to enter values*/
    @GetMapping("/addEduInfo")
    public String addEducationInfo(Model model) {
         /*Here we allow the user only has to enter 10 most recent education achivement information and
	    if the user or person tries to enter more than 10 information then submit button will get
	    disable so that they cannot enter more than ten information*/
        EduAchievements eduAchievements=new EduAchievements();
        model.addAttribute("allUser", resumeRepostory.findAll());
        model.addAttribute("disSubmit", eduAchievementsRepostory.count() >= 10);
        model.addAttribute("rowNumber", eduAchievementsRepostory.count());
        model.addAttribute("newEduInfo", eduAchievements);
        return "addEduInfo";
    }

    /*This method is used to check the validation for each values has been entered and if it is valid data then it will save it to data base table
   * also store and check  the record of the rows in data base table*/
    @PostMapping("/addEduInfo")
    public String addEducationInfo(@Valid @ModelAttribute("newEduInfo") EduAchievements eduAchievements,BindingResult bindingResult,@RequestParam("resumes") long resume_id,Model model) {

        if (bindingResult.hasErrors()) {
            // expect at least one educational info
            model.addAttribute("rowNumber", eduAchievementsRepostory.count());
            return "addEduInfo";
        }
        eduAchievements.setResume(resumeRepostory.findOne(resume_id));
        eduAchievementsRepostory.save(eduAchievements);
        model.addAttribute("rowNumber", eduAchievementsRepostory.count());
        return "dispEduInfo";

    }

    /*This method is used to modify the existing in forms then update data bas tables according to there modify fields */
    @RequestMapping("/updateEduInfo/{id}")
    public String updateEduInfo(@PathVariable("id") long id, Model model){
        model.addAttribute("newEduInfo", eduAchievementsRepostory.findOne(id));
        model.addAttribute("allUser",resumeRepostory.findAll());

        return "addEduInfo";
    }

    /*This method is used to delete the existing data  records from data base table and dispaly the rest of data which has been there*/
    @RequestMapping("/deleteEduInfo/{id}")
    public String delEduInfo(@PathVariable("id") long id){
        eduAchievementsRepostory.delete(id);
        return "redirect:/listEduInfo";
    }
    /*This method is used to display the existing data  records from data base table*/
    @RequestMapping("/listEduInfo")
    public String listEduInfo(Model model){
        model.addAttribute("searchEdu", eduAchievementsRepostory.findAll());
        return "listEduInfo";
    }
    /***********************************************************************************************************************/
    /*********************************************Work Experiences Information to add *Work Experiences to modify the existing and to delete the existing *Work Experiences information *********************************************************/
    /*This method is used to dispaly a form of  work Experiences to person to enter values*/
    @GetMapping("/addWorkExpInfo")
    public String addWorkExpiInfo(Model model) {
          /*Here we allow the user only has to enter 10 most recent work experience information and
	    if the user or person tries to enter more than 10 information then submit button will get
	    disable so that they cannot enter more than ten information*/
        WorkExperiences workExperiences=new WorkExperiences();
        model.addAttribute("allUser", resumeRepostory.findAll());
        model.addAttribute("disSubmit", workExperiencesRepostory.count() >= 10);
        model.addAttribute("rowNumber", workExperiencesRepostory.count());
        model.addAttribute("newWork",workExperiences);
        return "addWorkExpInfo";
    }

    /*This method is used to check the validation for each values has been entered and if it is valid data then it will save it to data base table
    * also store and check  the record of the rows in data base table*/
    @PostMapping("/addWorkExpInfo")
    public String addWorkExpiInfo(@Valid @ModelAttribute("newWork") WorkExperiences workExperiences,BindingResult bindingResult,@RequestParam("resumes") long resume_id,Model model) {

        if (bindingResult.hasErrors()) {
            return "addWorkExpInfo";
        }

        workExperiences.setResume(resumeRepostory.findOne(resume_id));
        workExperiencesRepostory.save(workExperiences);
//        model.addAttribute("rowNumber", workExperiencesRepostory.count());
        return "dispWorkExpInfo";
    }
    /*This method is used to modify the existing in forms then update data bas tables according to there modify fields */
    @RequestMapping("/updateExpInfo/{id}")
    public String updateWorkExp(@PathVariable("id") long id, Model model){
        model.addAttribute("allUser",resumeRepostory.findAll());
        model.addAttribute("newWork", workExperiencesRepostory.findOne(id));
        return "addWorkExpInfo";
    }
    /*This method is used to delete the existing data  records from data base table and dispaly the rest of data which has been there*/
    @RequestMapping("/deleteExpInfo/{id}")
    public String delWorkExpInfo(@PathVariable("id") long id){
        workExperiencesRepostory.delete(id);
        return "redirect:/listExpInfo";
    }
    /*This method is used to display the existing data  records from data base table*/
    @RequestMapping("/listExpInfo")
    public String listWorkExpInfo(Model model){
        model.addAttribute("searchExp", workExperiencesRepostory.findAll());
        return "listExpInfo";
    }
    /***************************************************************************************************************************/

    /***********************************************Skills Information to add Skills to modify the existing and to delete the existing Skills information****************************************************************************/
   /*This method is used to dispaly a form of  skills to person to enter values*/
    @GetMapping("/addSkillInfo")
    public String addSkilsInfo(Model model) {
        /*Here we allow the user only has to enter 20 skills information and
	    if the user or person tries to enter more than 20 information then submit button will get
	    disable so that they cannot enter more than ten information*/
        Skills skills=new Skills();
        model.addAttribute("allUser", resumeRepostory.findAll());
        model.addAttribute("disSubmit", skillsRepostory.count() >= 20);
        model.addAttribute("rowNumber", skillsRepostory.count());
        model.addAttribute("newSkill", skills);
        return "addSkillInfo";
    }
    /*This method is used to check the validation for each values has been entered and if it is valid data then it will save it to data base table
     * also store and check  the record of the rows in data base table*/
    @PostMapping("/addSkillInfo")
    public String addSkilsInfo(@Valid @ModelAttribute("newSkill") Skills skills,BindingResult bindingResult, @RequestParam("resumes") long resume_id,Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("rowNumber", skillsRepostory.count());
            return "addSkillInfo";
        }
        skills.setResume(resumeRepostory.findOne(resume_id));
        skillsRepostory.save(skills);
        model.addAttribute("rowNumber", skillsRepostory.count());
        return "dispSkillsInfo";
    }

    /*This method is used to modify the existing in forms then update data bas tables according to there modify fields */
    @RequestMapping("/updateSkillInfo/{id}")
    public String updateSkillInfo(@PathVariable("id") long id, Model model){
        model.addAttribute("newSkill", skillsRepostory.findOne(id));
        model.addAttribute("allUser",resumeRepostory.findAll());
        return "addSkillInfo";
    }
    /*This method is used to delete the existing data  records from data base table and dispaly the rest of data which has been there*/
    @RequestMapping("/deleteSkillInfo/{id}")
    public String delSkillInfo(@PathVariable("id") long id){
        skillsRepostory.delete(id);
        return "redirect:/listSkillInfo";
    }
    /*This method is used to display the existing data  records from data base table*/
    @RequestMapping("/listSkillInfo")
    public String listSkillInfo(Model model){
        model.addAttribute("searchSkill", skillsRepostory.findAll());
        return "listSkillInfo";
    }
    /***************************************************************************************************/
    /*******************************Result Info***************************************************************/

    @RequestMapping("/EditResumedetail/{id}")
    public String viewResume(@PathVariable("id") long id, Model model) {
        Resume resumeR=resumeRepostory.findOne(id);
        model.addAttribute("resumeR", resumeR);
        model.addAttribute("listEdu",eduAchievementsRepostory.findByResume(resumeR));
        model.addAttribute("listSkill",skillsRepostory.findByResume(resumeR));
        model.addAttribute("listExps",workExperiencesRepostory.findByResume(resumeR));
        return "ResultResumeInfo";
    }

    /*******************************************************************************************/
    @GetMapping("/SummerizedResume/{id}")
    public String summary(@PathVariable("id") long id,Model model) {
        Resume resumeR=resumeRepostory.findOne(id);
        model.addAttribute("resumeR", resumeR);
        model.addAttribute("listEdu",eduAchievementsRepostory.findByResume(resumeR));
        model.addAttribute("listSkill",skillsRepostory.findByResume(resumeR));
        model.addAttribute("listExps",workExperiencesRepostory.findByResume(resumeR));
        return "SummerizedResume";
    }

    @GetMapping("/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }
    @GetMapping("/loadcourse")
    public @ResponseBody String loadcourse()
    {

        Course course1 = new Course();
        course1.setCourseName("Sql");
        courseRepository.save(course1);

        Course course2 = new Course();
        course1.setCourseName("Java");
        courseRepository.save(course2);

        Course course3 = new Course();
        course1.setCourseName("Php");
        courseRepository.save(course3);

        Course course4 = new Course();
        course1.setCourseName("C#");
        courseRepository.save(course4);


        return "redirect:/addCourse";
    }

    @GetMapping("/addCourse")
    public String addCourse(Model model){
        model.addAttribute("course",new Course());
        return "addCourse";
    }
    @PostMapping("/addCourse")
    public String saveCourse(@ModelAttribute("course") Course  course)
    {
        courseRepository.save(course);
        return "redirect:/displayAll";
    }

    @GetMapping("/addcourseTostudent/{id}")
    public String addCourse(@PathVariable("id") long perID, Model model)
    {

        model.addAttribute("stud",resumeRepostory.findOne(new Long(perID)));
        model.addAttribute("courseList",courseRepository.findAll());

        return "addcourseTostudent";
    }
    @PostMapping("/addcourseTostudent/{id}")
    public String addStudentToCourse(@RequestParam("stud") String perID, @PathVariable("course") long id, Model model)
    {

        Course course = courseRepository.findOne(new Long(id));
        course.addResume(resumeRepostory.findOne(new Long(perID)));
        courseRepository.save(course);
        model.addAttribute("courseList",courseRepository.findAll());
        model.addAttribute("studList",resumeRepostory.findAll());
        return "redirect:/displayAll";
    }
    @RequestMapping("/displayAll")
    public String showAll(Model model)
    {
        model.addAttribute("gotmovies",resumeRepostory.count());
        model.addAttribute("gotactors",courseRepository.count());
        model.addAttribute("courseList",courseRepository.findAll());
        model.addAttribute("studList",resumeRepostory.findAll());
        return "dispCourseInfo";
    }



}
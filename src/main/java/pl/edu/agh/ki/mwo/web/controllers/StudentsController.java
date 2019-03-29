package pl.edu.agh.ki.mwo.web.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.SchoolClass;
import pl.edu.agh.ki.mwo.model.Student;
import pl.edu.agh.ki.mwo.persistence.DatabaseConnector;

@Controller
public class StudentsController {

	public String studentID = null;
	
    @RequestMapping(value="/Students")
    public String listStudent(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

    	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	
        return "studentsList";    
    }
    
    @RequestMapping(value="/AddStudent")
    public String displayAddStudentForm(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
 
       	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
       	
        return "studentForm";      
    }

    @RequestMapping(value="/CreateStudent", method=RequestMethod.POST)
    public String createStudent(@RequestParam(value="studentName", required=false) String name,
    		@RequestParam(value="studentSurname", required=false) String surname,
    		@RequestParam(value="studentPesel", required=false) String pesel,
    		@RequestParam(value="studentSchoolClass", required=false) String schoolClassId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

    	Student student = new Student();
    	student.setName(name);
    	student.setSurname(surname);
    	student.setPesel(pesel);

    	DatabaseConnector.getInstance().addStudent(student, schoolClassId);    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Nowy uczeń został dodany");

    	return "studentsList";
    }
   
    @RequestMapping(value="/DeleteStudent", method=RequestMethod.POST)
    public String deleteStudent(@RequestParam(value="studentId", required=false) String studentId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	DatabaseConnector.getInstance().deleteStudent(studentId);    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Uczeń został usunięty");
         	
    	return "studentsList";
    }

    @RequestMapping(value="/UpdStudent")
    public String displayAddStudentUpdForm(@RequestParam(value="studentId", required=false) String studentId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
 
    	studentID = studentId;
       	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
       	
    	String name = DatabaseConnector.getInstance().getStudent(studentID).getName();
    	String surname = DatabaseConnector.getInstance().getStudent(studentID).getSurname();
    	String pesel = DatabaseConnector.getInstance().getStudent(studentID).getPesel();
    	
    	model.addAttribute("student", DatabaseConnector.getInstance().getStudent(studentID)); 
    	model.addAttribute("studentName", name); 
    	model.addAttribute("studentSurname", surname); 
    	model.addAttribute("studentPesel", pesel); 
       	
        return "studentUpdForm";      
    }

    @RequestMapping(value="/UpdateStudent", method=RequestMethod.POST)
    public String updateStudent(@RequestParam(value="studentName", required=false) String name,
    		@RequestParam(value="studentSurname", required=false) String surname,
    		@RequestParam(value="studentPesel", required=false) String pesel,
    		@RequestParam(value="studentSchoolClass", required=false) String schoolClassId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	Student student = DatabaseConnector.getInstance().getStudent(studentID);
    	student.setName(name); 
    	student.setSurname(surname);
    	student.setPesel(pesel);
    	DatabaseConnector.getInstance().addStudent(student, schoolClassId);  
    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Uczeń został updejtowany");

    	return "studentsList";
    }
}
package com.servlet.student;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;

import com.model.ClassX;
import com.model.Student;
import com.service.classes.ClassServiceImpl;
import com.service.student.StudentServiceImpl;
import com.util.ImageUtil;

/**
 * Servlet implementation class AddStudentServlet
 */
@MultipartConfig
@WebServlet("/add-student")
public class AddStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("admin") == null) {
			response.sendRedirect("admin-login");
		} else {
			List<ClassX> classes = ClassServiceImpl.getInstance().getClasses();
			request.setAttribute("classes", classes);
			RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/add-student.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("admin") == null) {
			response.sendRedirect("admin-login");
		} else {
			//Upload Profile Picture
			Part picture =request.getPart("profile_pic");
			String profilePic = ImageUtil.uploadStudentProfilePic(picture);
			
			String classId = request.getParameter("class_id");
			int classIdInt = Integer.parseInt(classId); 
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			String phoneNumber = request.getParameter("phone_number");
			String password = request.getParameter("password");
			String address = request.getParameter("address");
			String dateOfBirth = request.getParameter("date_of_birth");
			String gender = request.getParameter("gender");
			String bio = request.getParameter("bio");

			//convert date of birth DD-MM-YYYY to YYYY-MM-DD
			String[] dateOfBirthArray = dateOfBirth.split("-");
			String dateOfBirthConverted = dateOfBirthArray[2] + "-" + dateOfBirthArray[1] + "-" + dateOfBirthArray[0];
			

			Student student = new Student(classIdInt, firstName, lastName, email, phoneNumber, password, address, dateOfBirthConverted, gender, bio, profilePic);
			Boolean result = StudentServiceImpl.getInstance().addStudent(student);

			if (result) {
				response.sendRedirect("students?add=success");
			} else {
				response.sendRedirect("students?add=fail");
			}
		}
	}

}

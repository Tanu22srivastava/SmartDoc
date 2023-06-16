package com.example.demo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.DatabaseFile;
import com.example.demo.model.PersonalDtls;
import com.example.demo.model.UserDtls;
import com.example.demo.model.DatabaseFile.FileType;
import com.example.demo.repositary.DatabaseFileRepository;
import com.example.demo.repositary.UserRepositary;
import com.example.demo.service.PersonalService;
import com.example.demo.repositary.personalRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

	@Autowired
	private UserRepositary userRepo;

	@Autowired
	private DatabaseFileRepository fileRepo;

	@Autowired
	private PersonalService personalService;

	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@Autowired
	private personalRepository personalRepository;

	@ModelAttribute
	private void userDetails(Model model, Principal p) {
		if (p != null) {
			String email = p.getName();
			UserDtls user = userRepo.findByEmail(email);

			PersonalDtls puser = personalRepository.findById(user.getId());
			// PersonalDtls puser = personalRepository.findByUser(user);

			model.addAttribute("user", user);
			model.addAttribute("puser", puser);

			List<DatabaseFile> awardsFiles = new ArrayList<>();
			List<DatabaseFile> achievementsFiles = new ArrayList<>();
			List<DatabaseFile> researchFiles = new ArrayList<>();
			List<DatabaseFile> bookFiles = new ArrayList<>();
			List<DatabaseFile> fdpFiles = new ArrayList<>();
			List<DatabaseFile> sttpFiles = new ArrayList<>();
			List<DatabaseFile> qipFiles = new ArrayList<>();
			List<DatabaseFile> workshopFiles = new ArrayList<>();

			List<DatabaseFile> files = fileRepo.findByUser(user);
			try {
				for (DatabaseFile file : files) {
					FileType fileType = file.getType();
					// System.out.println(file + "Type:\t" + fileType);
					switch (fileType) {
						case AWARD:
							// System.out.println("File Type is Award");
							awardsFiles.add(file);
							break;
						case ACHIEVEMENT:
							achievementsFiles.add(file);
							break;
						case RESEARCH_PAPER:
							researchFiles.add(file);
							break;
						case BOOK_OR_CHAPTER:
							bookFiles.add(file);
							break;
						case FDP:
							fdpFiles.add(file);
							break;
						case STTP:
							sttpFiles.add(file);
							break;
						case QIP:
							qipFiles.add(file);
							break;
						case WORKSHOP:
							workshopFiles.add(file);
							break;
						default:
							System.out.println("Unknown file type");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!awardsFiles.isEmpty()) {
				// System.out.println("Awards Files are not empty");
				model.addAttribute("awardsFiles", awardsFiles);

			}
			if (!achievementsFiles.isEmpty()) {
				model.addAttribute("achievementsFiles", achievementsFiles);
			}

			if (!researchFiles.isEmpty()) {
				model.addAttribute("researchFiles", researchFiles);
			}

			if (!bookFiles.isEmpty()) {
				model.addAttribute("bookFiles", bookFiles);
			}

			if (!fdpFiles.isEmpty()) {
				model.addAttribute("fdpFiles", fdpFiles);
			}

			if (!sttpFiles.isEmpty()) {
				model.addAttribute("sttpFiles", sttpFiles);
			}

			if (!qipFiles.isEmpty()) {
				model.addAttribute("qipFiles", qipFiles);
			}

			if (!workshopFiles.isEmpty()) {
				model.addAttribute("workshopFiles", workshopFiles);
			}
		} else {
			model.addAttribute("user", null);
		}

	}

	@PostMapping("/updateTeacher")
	public String updateUser(Principal p,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "erpId", required = true) String erpId,
			@RequestParam(value = "offEmail", required = true) String offEmail,
			@RequestParam(value = "perEmail", required = true) String perEmail,
			@RequestParam(value = "dept", required = true) String dept,
			@RequestParam(value = "whatsNumber", required = true) String whatsNumber,
			@RequestParam(value = "mobileNumber", required = true) String mobileNumber,
			@RequestParam(value = "dob", required = true) String dob,
			@RequestParam(value = "gender", required = true) String gender,
			@RequestParam(value = "expInd", required = false) String expInd,
			@RequestParam(value = "expAcd", required = false) String expAcd,
			@RequestParam(value = "doj", required = false) String doj,
			@RequestParam(value = "dol", required = false) String dol,
			@RequestParam(value = "googleId", required = false) String googleId,
			@RequestParam(value = "scopusId", required = false) String scopusId,
			@RequestParam(value = "sciId", required = false) String sciId,
			@RequestParam(value = "curAdd", required = true) String curAdd,
			@RequestParam(value = "currCity", required = true) String currCity,
			@RequestParam(value = "currState", required = true) String currState,
			@RequestParam(value = "currCunt", required = true) String currCunt,
			@RequestParam(value = "currPin", required = true) String currPin,
			@RequestParam(value = "perAdd", required = true) String perAdd,
			@RequestParam(value = "perCity", required = true) String perCity,
			@RequestParam(value = "perState", required = true) String perState,
			@RequestParam(value = "perCunt", required = true) String perCunt,
			@RequestParam(value = "perPin", required = true) String perPin) {

		String email = p.getName();
		UserDtls user = userRepo.findByEmail(email);
		try {
			PersonalDtls puser = personalRepository.findByUser(user);

			PersonalDtls existingPersonalDtls = personalRepository.findByUser(puser.getUser());

			// update the fields with the new values
			existingPersonalDtls.setName(name);
			existingPersonalDtls.setErpId(erpId);
			existingPersonalDtls.setOffEmail(offEmail);
			existingPersonalDtls.setPerEmail(perEmail);
			existingPersonalDtls.setDept(dept);
			existingPersonalDtls.setWhatsNumber(whatsNumber);
			existingPersonalDtls.setMobileNumber(mobileNumber);
			existingPersonalDtls.setGender(gender);
			existingPersonalDtls.setDob(dob);
			existingPersonalDtls.setExpInd(expInd);
			existingPersonalDtls.setExpAcd(expAcd);
			existingPersonalDtls.setDoj(doj);
			existingPersonalDtls.setDol(dol);
			existingPersonalDtls.setGoogleId(googleId);
			existingPersonalDtls.setScopusId(scopusId);
			existingPersonalDtls.setSciId(sciId);
			existingPersonalDtls.setCurAdd(curAdd);
			existingPersonalDtls.setCurrCity(currCity);
			existingPersonalDtls.setCurrState(currState);
			existingPersonalDtls.setCurrCunt(currCunt);
			existingPersonalDtls.setCurrPin(currPin);
			existingPersonalDtls.setPerAdd(perAdd);
			existingPersonalDtls.setPerCity(perCity);
			existingPersonalDtls.setPerState(perState);
			existingPersonalDtls.setPerCunt(perCunt);
			existingPersonalDtls.setPerPin(perPin);

			// save the updated personal details object
			personalRepository.save(existingPersonalDtls);
			return "redirect:/teacher/personalInfo";

		} catch (Exception e) {
			System.err.println("Error in updating user details\n\n\n\n");
			e.printStackTrace();
		}
		return "redirect:/teacher/update-user-details";
	}

	@GetMapping("/files")
	public String listFiles(Model model, Principal principal) {
		/*
		 * to use the above mapping in a web page, use the following code
		 * <table>
		 * <thead>
		 * <tr>
		 * <th>Name</th>
		 * <th>Type</th>
		 * <th>Actions</th>
		 * </tr>
		 * </thead>
		 * <tbody>
		 * <tr th:each="file : ${files}">
		 * <td th:text="${file.fileName}"></td>
		 * <td th:text="${file.fileType}"></td>
		 * <td>
		 * <a th:href="@{/download/{id}(id=${file.id})}"
		 * class="btn btn-primary">Download</a>
		 * <span th:if="${file.user.id == currentUser.id}">
		 * <a href="#" class="btn btn-primary" data-toggle="modal"
		 * data-target="#uploadModal">Upload New Version</a>
		 * </span>
		 * </td>
		 * </tr>
		 * </tbody>
		 * </table>
		 * 
		 */
		UserDtls user = userRepo.findByEmail(principal.getName());
		List<DatabaseFile> files = user.getFiles();
		model.addAttribute("files", files);
		return "files";
	}

	@GetMapping("/")
	public String home() {
		return "user/teacherFiles/teacher";
	}

	@GetMapping("/teacher-dashboard")
	public String dashboard() {
		return "user/teacherFiles/teacherDashboard";
	}

	@GetMapping("/personalInfo")
	public String personalInfo() {
		return "user/teacherFiles/personalInfo";
	}

	@GetMapping("/research")
	public String research() {
		return "user/teacherFiles/researchPublications";
	}

	@GetMapping("/awards")
	public String awards() {
		return "user/teacherFiles/awardsAchievements";
	}

	@GetMapping("/interactions")
	public String interactions() {
		return "user/teacherFiles/interactions";
	}

	@GetMapping("/fdp")
	public String fdp() {
		return "user/teacherFiles/fdp";
	}

	@GetMapping("/settings")
	public String settings() {
		return "user/teacherFiles/settings";
	}

	@GetMapping("/changePass")
	public String loadChangePassword() {
		return "user/teacherFiles/settings";
	}

	@GetMapping("/update-user-details")
	public String updateDetails() {
		return "user/teacherFiles/detailUpdateForm";
	}

	@PostMapping("/personalUser")
	public String personalUser(@ModelAttribute PersonalDtls puser) {
		PersonalDtls personalDtls = personalService.personalUser(puser);

		if (personalDtls != null) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "user/teacherFiles/personalInfo";
		}
		return "user/teacherFiles/detailUpdateForm";

	}

	@PostMapping("/updatePassword")
	public String changePassword(Principal p, @RequestParam("oldPass") String oldPass,
			@RequestParam("newPass") String newPass, HttpSession session) {

		String email = p.getName();
		UserDtls loginUser = userRepo.findByEmail(email);

		boolean f = passwordEncode.matches(oldPass, loginUser.getPassword());

		if (f) {
			loginUser.setPassword(passwordEncode.encode(newPass));
			UserDtls updatePasswordUser = userRepo.save(loginUser);
			if (updatePasswordUser != null) {
				System.out.println("password changed successfully");
			} else {
				System.out.println("something went wrong");
			}

		} else {
			System.out.println("incorrect password");

		}

		return "redirect:/teacher/settings";

	}
}
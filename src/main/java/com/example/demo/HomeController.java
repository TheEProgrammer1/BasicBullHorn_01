package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController
{
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model)
    {
        model.addAttribute("user", new User());
        return  "registration";
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model)
    {
        model.addAttribute("user", user);
        if(result.hasErrors())
        {
            return "registration";
        }
        else
        {
            userService.saveUser(user);
            model.addAttribute("message", "userAcount Successfully Created");
        }


        return "list";
    }




    @RequestMapping("/")
    public String listCourse(Model model)
    {
        model.addAttribute("messages", messageRepository.findAll());

        return "list";
    }

    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }

    @RequestMapping("/admin")
    public String admin()
    {
        return "list";
    }

    @GetMapping("/add")
    public String messageForm(Model model, HttpServletRequest request, Authentication authentication, Principal principal)
    {
        Boolean isAdmin = request.isUserInRole("Admin");
        Boolean isUer = request.isUserInRole("User");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = principal.getName();
        Message newMessage = new Message();
        newMessage.setSentby(username);
        System.out.println("HELLO I AM HERE" + newMessage.getSentby());

        model.addAttribute("message", newMessage);
        return "messageForm";
    }

    @PostMapping("/process")
    public String processForm(@Valid Message message, BindingResult result)
    {
        if(result.hasErrors())
        {
            return "messageForm";
        }
        messageRepository.save(message);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showMessage(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("message", messageRepository.findOne(id));
        return "show";
    }
}

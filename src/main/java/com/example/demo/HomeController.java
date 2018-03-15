package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Controller
public class HomeController
{
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    CloudinaryConfig cloudc;


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

    @RequestMapping("/mymessages")
    public String myMessages(Model model, HttpServletRequest request, Authentication authentication, Principal principal)
    {
        if(principal != authentication)
        {
            Boolean isAdmin = request.isUserInRole("ADMIN");
            Boolean isUser = request.isUserInRole("USER");
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = principal.getName();
        }

      // UserMessagesRepository userMessagesRepository = userService.(principal.getName()).getUserMessageRespository();

        //model.addAttribute("messages", userMessagesRepository);

        return "userMessages";
    }

    @RequestMapping("/")
    public String listCourse(Model model, HttpServletRequest request, Authentication authentication, Principal principal)
    {

        if(principal != authentication)
        {
            Boolean isAdmin = request.isUserInRole("ADMIN");
            Boolean isUser = request.isUserInRole("USER");
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = principal.getName();
        }

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
    /*
    @PostMapping("/add")
    public String newUser(@ModelAttribute Message message, @RequestParam("file")MultipartFile file)
    {
        if(file.isEmpty())
        {
            return "redirect:/add";
        }
        try
        {
            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            message.setImage(uploadResult.get("url").toString());
            System.out.println("I am here");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "redirect:/add";
        }

        return "redirect:/add";
    }*/
    @GetMapping("/add")
    public String messageForm(Model model, HttpServletRequest request, Authentication authentication, Principal principal)
    {
        Message newMessage = new Message();

        Boolean isAdmin = request.isUserInRole("Admin");
        Boolean isUer = request.isUserInRole("User");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = principal.getName();

        newMessage.setSentby(username);
        //System.out.println("HELLO I AM HERE" + newMessage.getSentby());

        model.addAttribute("message", newMessage);
        return "messageForm";
    }

    @PostMapping("/process")
    public String processForm(@ModelAttribute Message message, @RequestParam("file")MultipartFile file, BindingResult result)
    {
        if(result.hasErrors())
        {
            return "messageForm";
        }

        if(file.isEmpty())
        {
           message.setImage(null);
           messageRepository.save(message);
           return "redirect:/";
        }
        try
        {
            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            message.setImage(uploadResult.get("url").toString());
            System.out.println("I am here");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "redirect:/add";
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

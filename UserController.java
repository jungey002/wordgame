package spring_word_game.spring_word_game;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private WordRepository wordRepository;
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return "redirect:/"; // Redirect to home page
    }
    @GetMapping("/dashboard")
    public String showForm(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        String winmessage = (String) session.getAttribute("win_message");
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("levels", new String[]{"Easy", "Medium", "Hard"});
            model.addAttribute("selectedLevel", "");
            model.addAttribute("user", user);
            model.addAttribute("message",winmessage);
            return "word-form";
        } else {
            return "redirect:/";
        }

    }
    @PostMapping("/word")
    public String getWord(@ModelAttribute("selectedLevel") String selectedLevel,
                          Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        session.setAttribute("win_message", null);
        if (user != null) {
            Word word = wordRepository.findRandomWordByLevel(selectedLevel);
            model.addAttribute("word", word);
            session.setAttribute("word", word);
            model.addAttribute("user", user);
            // return "wordDetails";
            return "redirect:/showWord";
        } else {
            return "redirect:/";
        }

    }
    @GetMapping("/showWord")
    public String showWord(HttpSession session, Model model) {


        User user = (User) session.getAttribute("user");
        if (user != null) {
            Word wordarray = (Word) session.getAttribute("word");
            model.addAttribute("GivenHints", wordarray.getHints());
            model.addAttribute("GivenImage", wordarray.getImage());
            model.addAttribute("user", user);
            return "word-input";
        } else {
            return "redirect:/";
        }
    }
    @PostMapping("/getWord")
    public String login(@RequestParam String word, HttpSession session, Model
            model) {

        User user = (User) session.getAttribute("user");
        if (user != null) {
            Word wordarray = (Word) session.getAttribute("word");
            model.addAttribute("GivenHints", wordarray.getHints());
            model.addAttribute("GivenImage", wordarray.getImage());

            if(word != null && wordarray.getWordName().toLowerCase().equals(word.toLowerCase())) {
                model.addAttribute("message", "Congratulations You win");
                model.addAttribute("user", user);
                //return "word-input";
                Integer score  = Math.toIntExact(user.getScore() + 10);
                return "redirect:/Score/"+user.getId()+"/"+score;
            } else {
                model.addAttribute("message", "Sorry!! You Loose");
                model.addAttribute("user", user);
                return "word-input";
            }

        } else {
            return "redirect:/";
        }


    }





}


package fiap.com.Cp5_Java;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class TranslateController {

    private final ChatModel chatModel;

    @Autowired
    public TranslateController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/")
    public String showChatPage() {
        return "index"; // Nome do arquivo HTML
    }

    @PostMapping("/translate")
    public String translate(
            @RequestParam("prompt") String prompt,
            Model model
    ) {

        String templateString = """
                Você é um tradutor assistente profissional.
                Traduza o seguinte texto de português para espanhol.
                Seja ético e moral em suas traduções, recusando-se a 
                traduzir conteúdo odioso, discriminatório ou prejudicial.
                
                Texto para traduzir: {prompt}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(templateString);
        Prompt finalPrompt = promptTemplate.create(Map.of("prompt", prompt));

        String translation = chatModel.call(finalPrompt)
                .getResult()
                .getOutput()
                .getText();

        model.addAttribute("originalPrompt", prompt);
        model.addAttribute("translation", translation);

        return "index";
    }
}
package com.devternity.slidy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrependEndpoint {

    @Autowired
    S3Bucket s3Bucket;

    @PostMapping("/prepend")
    Response prepend(Body $) throws Exception {

//        Map<String, String> params = ImmutableMap.<String, String>builder()
//                .put("deckUrl", "http://localhost:8080/mintoslatcraft2-160815132905.pdf")
//                .put("templateUrl", "http://a56d6a2a.ngrok.io/template/index.html?title=bogota")
//                .build();

        Deck deck = new Deck($.deckUrl);
        Deck.Dimensions dimensions = deck.dimensions();

        SlideHtmlTemplate slideHtmlTemplate = new SlideHtmlTemplate($.templateUrl);
        Slide slide = slideHtmlTemplate.slide(dimensions);

        Deck deckWithANewSlide = deck.prepend(slide);
        Output output = deckWithANewSlide.output(s3Bucket);
        output.write();

        Response response = new Response();
        response.deckUrl = output.writtenTo().toString();
        return response;
    }

}

class Body {

    String deckUrl;
    String templateUrl;

}

class Response {

    String deckUrl;

}

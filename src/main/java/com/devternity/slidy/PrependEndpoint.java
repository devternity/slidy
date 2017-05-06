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

        Deck deck = new Deck(new Url($.deckUrl, "Deck url"));
        Deck.Dimensions dimensions = deck.dimensions();

        SlideHtmlTemplate slideHtmlTemplate = new SlideHtmlTemplate(new Url($.templateUrl, "Template url"));
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

package languagetool_nlp


import grails.rest.*
import grails.converters.*

//import io.swagger.annotations.*
import com.wordnik.swagger.annotations.*

@Api(value = "Tokenization services", 
    description = "Tokenization services for Ukrainian language",
    produces = 'application/json',
    consumes = 'application/json'
)
class TokenizeController extends ControllerBase {

    def tokenizeService


    @ApiOperation(value = "Tokenizes the text into sentences and then into words", 
                httpMethod = "POST",
                response = Response.class)
    @ApiResponses([
        @ApiResponse(code = 400, message = "Invalid body provided"),
        @ApiResponse(code = 400, message = "Text limit exceeded")
    ])
    @ApiImplicitParams([
        @ApiImplicitParam(name = 'body', paramType = 'body', required = true, dataType='InputBody', 
            value='Body text; e.g<br>{"text": "І.А. Іванов прийшов додому. І з\'їв 2 тис. кавунів."}')
    ])
    def save() {

        if( ! validateRequest(request) )
            return

        try {
            def tokens = tokenizeService.tokenize(request.JSON, params)

            def response = new Response(tokens: tokens)

            updateNotes(response)

            render response as JSON
        }
        catch(Exception e) {
            e.printStackTrace()
            render(status: 500, text: "Internal error: " + e.getMessage())
            return
        }

    }

    static class Response {
        List<String> tokens
        String notes
    }
}

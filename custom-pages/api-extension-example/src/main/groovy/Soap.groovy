import groovy.json.JsonBuilder

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.bonitasoft.console.common.server.page.*

import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse


class Soap implements RestApiController{

    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {
        String yearParam = request.getParameter "year"
        Map<String, String> result = [:]
        if (yearParam == null) {
            result.put "error", "the attribute year is missing"
            apiResponseBuilder.withResponseStatus(HttpServletResponse.SC_BAD_REQUEST)
        } else {
            try {
                def currentYear = yearParam as int
                SOAPClient client = new SOAPClient('http://www.holidaywebservice.com/Holidays/US/Dates/USHolidayDates.asmx')
                SOAPResponse response = client.send(SOAPAction:'http://www.27seconds.com/Holidays/US/Dates/GetMothersDay') {
                    body {
                        GetMothersDay('xmlns':'http://www.27seconds.com/Holidays/US/Dates/') {
                            year(currentYear)
                        }
                    }
                }
                result.put "mothersDay", response.GetMothersDayResponse.GetMothersDayResult.text()
            } catch (NumberFormatException e) {
                result.put "error", "the attribute year must be an integer"
                apiResponseBuilder.withResponseStatus(HttpServletResponse.SC_BAD_REQUEST)
            }
        }
        JsonBuilder builder = new JsonBuilder(result)
        apiResponseBuilder.with {
            withResponse builder.toPrettyString()
            build()
        }
    }

}

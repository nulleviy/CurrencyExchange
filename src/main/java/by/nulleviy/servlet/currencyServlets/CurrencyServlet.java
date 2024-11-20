package by.nulleviy.servlet.currencyServlets;

import by.nulleviy.dto.CurrencyDto;
import by.nulleviy.entity.exception.CustomException;
import by.nulleviy.entity.exception.ErrorResponse;
import by.nulleviy.service.CurrencyService;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getPathInfo().replace("/","").toUpperCase();
        if(code.length() != 3){
            resp.setStatus(400);
            mapper.writeValue(resp.getWriter(),new ErrorResponse(resp.getStatus(),"Код валюты отсутствует"));
        }
        if(currencyService.findByCode(code).isEmpty()){
            resp.setStatus(404);
            mapper.writeValue(resp.getWriter(),new ErrorResponse(resp.getStatus(),"Валюта не найдена"));
        }
        try {
            CurrencyDto currencyDto = currencyService.findByCode(code).get();
            mapper.writeValue(resp.getWriter(),currencyDto);
        }catch(CustomException e){
            resp.setStatus(500);
        }
}
}

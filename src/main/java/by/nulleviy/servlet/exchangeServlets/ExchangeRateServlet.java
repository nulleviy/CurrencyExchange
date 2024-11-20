package by.nulleviy.servlet.exchangeServlets;

import by.nulleviy.entity.exception.CustomException;
import by.nulleviy.entity.exception.ErrorResponse;
import by.nulleviy.service.ExchangeRateService;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/exchange_rates/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getPathInfo();
        String firstPart = url.substring(1, 4).toUpperCase();
        String secondPart = url.substring(4).toUpperCase();
        if(exchangeRateService.findByCodes(firstPart, secondPart).isEmpty()){
            resp.setStatus(404);
            mapper.writeValue(resp.getWriter(),new ErrorResponse(resp.getStatus(),"Обменный курс для пары не найден"));
        }
        if(firstPart.length()+secondPart.length()!=6){
            resp.setStatus(400);
            mapper.writeValue(resp.getWriter(),new ErrorResponse(resp.getStatus(),"Коды валют пары отсутствуют в адресе"));
        }
        try{
            mapper.writeValue(resp.getWriter(),exchangeRateService.findByCodes(firstPart,secondPart).get());
        }catch(CustomException e){
            resp.setStatus(500);
            mapper.writeValue(resp.getWriter(),new CustomException("Что-то не так с гетом"));
        }
    }
}

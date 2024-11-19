package by.nulleviy.servlet.exchangeServlets;

import by.nulleviy.dto.ExchangeResponseDto;
import by.nulleviy.entity.exception.CustomException;
import by.nulleviy.service.ExchangeRateService;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeCurrenciesServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("from").toUpperCase();
        String targetCurrencyCode = req.getParameter("to").toUpperCase();
        String amountString = req.getParameter("amount");

        try{
            ExchangeResponseDto exchangeResponseDto = exchangeRateService.exchange(baseCurrencyCode,targetCurrencyCode,amountString);
            objectMapper.writeValue(resp.getWriter(), exchangeResponseDto);
        }catch(CustomException e){
            resp.setStatus(500);
            objectMapper.writeValue(resp.getWriter(),new CustomException("Что-то с гетом"));
        }
    }
}

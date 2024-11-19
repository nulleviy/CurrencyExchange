package by.nulleviy.dao;

import by.nulleviy.Utils.ConnectionManager;
import by.nulleviy.entity.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class CurrencyDao {


    private final static String FIND_ALL = """
            SELECT *
            FROM currencies
            """;
    private final static String FIND_BY_CODE = FIND_ALL + """
            WHERE code = ?
            """;
    private final static String FIND_BY_ID = FIND_ALL + """
            WHERE id = ?
            """;
    private final static String SAVE = """
            INSERT INTO currencies (code, fullname, sign)
            VALUES (?,?,?)
            """;

    public Currency findByCode(String code){
        try (Connection connection = ConnectionManager.get();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE)) {

            Currency currency = null;
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                currency = buildCurrency(resultSet);
            }
            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Currency findById(int id){
        try (Connection connection = ConnectionManager.get();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {

            Currency currency = null;
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                currency = buildCurrency(resultSet);
            }
            return currency;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Currency> findAll(){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Currency> currencyList = new ArrayList<>();

            while(resultSet.next()){
                currencyList.add(buildCurrency(resultSet));
            }
            return currencyList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save (Currency currency){
        try (Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {

            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    @SneakyThrows
    public Currency buildCurrency(ResultSet resultSet) {
        return new Currency(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("fullName"),
                resultSet.getString("sign")
        );
    }
}

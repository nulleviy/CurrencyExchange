package by.nulleviy.dao.impl;

import by.nulleviy.Utils.ConnectionManager;
import by.nulleviy.entity.Currency;
import lombok.SneakyThrows;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {

    private final static String FIND_ALL_SQL = """
            SELECT * FROM currencies
            """;

    public static List<Currency> findAll(){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
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

    @SneakyThrows
    private static Currency buildCurrency(ResultSet resultSet) {
        return new Currency(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("fullName"),
                resultSet.getString("sign")
        );
    }
}

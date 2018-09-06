package com.mealplanner.dal.test;

import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.mealplanner.dal.MealRepository;
import com.mealplanner.domain.Meal;

@ExtendWith(MockitoExtension.class)
public class MealRepositoryTest {

    private static final String MEAL_ID = "meal1";
    private static final String USER_ID = "user1";

    @Mock
    private DynamoDBMapper mapper;

    @Mock
    private PaginatedQueryList<Meal> paginatedQueryList;

    @Mock
    private Meal mockMeal;

    @InjectMocks
    private MealRepository mealRepository;

    @Test
    public void meal_is_returned_if_one_result_is_found() {
        when(mapper.query(Mockito.eq(Meal.class), Mockito.any())).thenReturn(paginatedQueryList);
        when(paginatedQueryList.size()).thenReturn(1);
        when(paginatedQueryList.get(0)).thenReturn(mockMeal);

        final Meal meal = mealRepository.get(MEAL_ID, USER_ID);

        Assertions.assertThat(meal).isEqualTo(mockMeal);
    }
}

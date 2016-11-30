package com.mariebyleen.weather.current_conditions;


import com.mariebyleen.weather.current_conditions.mapper.CurrentConditionsMapper;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponseMain;
import com.mariebyleen.weather.current_conditions.view_model.CurrentConditionsViewModel;
import com.mariebyleen.weather.current_conditions.view_model.UpdateService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertEquals;

public class DataFormattingTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Mock
    private UpdateService updateService;

    private CurrentConditionsMapper mapper;
    private CurrentConditionsResponse testResponse;
    private CurrentConditionsViewModel viewModel;

    @Before
    public void init() {
        testResponse = new CurrentConditionsResponse();
        mapper = new CurrentConditionsMapper();
        viewModel = new CurrentConditionsViewModel(updateService, mapper);
    }

    @Test
    public void tempFormattedWithTemperatureLabel() {
        setFakeTemperature(287.1);

        viewModel.onNext(testResponse);
        String result = viewModel.getTemperature();

        assertEquals(result, "Temperature: 287.1");
    }

    private CurrentConditionsResponse setFakeTemperature(double temperature) {
        CurrentConditionsResponseMain main = new CurrentConditionsResponseMain();
        main.setTemp(temperature);
        testResponse.setMain(main);
        return testResponse;
    }
}

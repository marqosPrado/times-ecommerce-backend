package br.com.marcosprado.timesbackend.utils;

import br.com.marcosprado.timesbackend.aggregate.StateAggregate;
import br.com.marcosprado.timesbackend.domain.State;

public class StateMapper {

    public static StateAggregate toAggregate(State state) {
        return new StateAggregate(state.getState());
    }
}

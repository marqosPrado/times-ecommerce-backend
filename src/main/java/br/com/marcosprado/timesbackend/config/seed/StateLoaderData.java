package br.com.marcosprado.timesbackend.config.seed;

import br.com.marcosprado.timesbackend.aggregate.StateAggregate;
import br.com.marcosprado.timesbackend.repository.StateRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StateLoaderData implements ApplicationRunner {

    private final StateRepository stateRepository;

    public StateLoaderData(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            if (stateRepository.count() == 0) {
                List<StateAggregate> states = List.of(
                        new StateAggregate("Acre"),
                        new StateAggregate("Alagoas"),
                        new StateAggregate("Amapá"),
                        new StateAggregate("Amazonas"),
                        new StateAggregate("Bahia"),
                        new StateAggregate("Ceará"),
                        new StateAggregate("Distrito Federal"),
                        new StateAggregate("Espírito Santo"),
                        new StateAggregate("Goiás"),
                        new StateAggregate("Maranhão"),
                        new StateAggregate("Mato Grosso"),
                        new StateAggregate("Mato Grosso do Sul"),
                        new StateAggregate("Minas Gerais"),
                        new StateAggregate("Pará"),
                        new StateAggregate("Paraíba"),
                        new StateAggregate("Paraná"),
                        new StateAggregate("Pernambuco"),
                        new StateAggregate("Piauí"),
                        new StateAggregate("Rio de Janeiro"),
                        new StateAggregate("Rio Grande do Norte"),
                        new StateAggregate("Rio Grande do Sul"),
                        new StateAggregate("Rondônia"),
                        new StateAggregate("Roraima"),
                        new StateAggregate("Santa Catarina"),
                        new StateAggregate("São Paulo"),
                        new StateAggregate("Sergipe"),
                        new StateAggregate("Tocantins")
                );

                stateRepository.saveAll(states);
            }
        }catch (Exception e){
            throw new RuntimeException("Erro ao tentar persistir dados de estados: ", e);
        }
    }
}

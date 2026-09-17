import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Principal {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DecimalFormat FORMATO_NUMERO;

    static {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("pt", "BR"));
        FORMATO_NUMERO = new DecimalFormat("#,##0.00", simbolos);
    }

    public static void main(String[] args) {
        List<Funcionario> funcionarios = new ArrayList<>();

        // 3.1 - Inserir todos os funcionários
        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        funcionarios.add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        funcionarios.add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));

        // 3.2 - Remover João
        funcionarios.removeIf(funcionario -> funcionario.getNome().equals("João"));

        // 3.3 - Imprimir todos os funcionários
        System.out.println("===== FUNCIONÁRIOS =====");
        funcionarios.forEach(Principal::imprimirFuncionario);

        // 3.4 - Aumento de 10%
        BigDecimal aumento = new BigDecimal("1.10");

        funcionarios.forEach(funcionario ->
                funcionario.setSalario(
                        funcionario.getSalario().multiply(aumento)
                )
        );

        // 3.5 - Agrupar funcionários por função
        Map<String, List<Funcionario>> funcionariosPorFuncao =
                funcionarios.stream()
                        .collect(Collectors.groupingBy(
                                Funcionario::getFuncao,
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        // 3.6 - Imprimir funcionários agrupados por função
        System.out.println("\n===== FUNCIONÁRIOS POR FUNÇÃO =====");
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("\nFunção: " + funcao);
            lista.forEach(funcionario -> System.out.println("- " + funcionario.getNome()));
        });

        // O enunciado não possui item 3.7

        // 3.8 - Aniversariantes dos meses 10 e 12
        System.out.println("\n===== ANIVERSARIANTES DOS MESES 10 E 12 =====");
        funcionarios.stream()
                .filter(funcionario -> {
                    int mes = funcionario.getDataNascimento().getMonthValue();
                    return mes == 10 || mes == 12;
                })
                .forEach(funcionario ->
                        System.out.println(funcionario.getNome() + " - "
                                + funcionario.getDataNascimento().format(FORMATO_DATA))
                );

        // 3.9 - Funcionário com maior idade
        System.out.println("\n===== FUNCIONÁRIO COM MAIOR IDADE =====");
        funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento))
                .ifPresent(funcionario -> {
                    int idade = Period.between(
                            funcionario.getDataNascimento(),
                            LocalDate.now()
                    ).getYears();

                    System.out.println("Nome: " + funcionario.getNome());
                    System.out.println("Idade: " + idade);
                });

        // 3.10 - Funcionários em ordem alfabética
        System.out.println("\n===== ORDEM ALFABÉTICA =====");
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .forEach(funcionario -> System.out.println(funcionario.getNome()));

        // 3.11 - Total dos salários
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("\n===== TOTAL DOS SALÁRIOS =====");
        System.out.println("R$ " + FORMATO_NUMERO.format(totalSalarios));

        // 3.12 - Quantidade de salários mínimos
        BigDecimal salarioMinimo = new BigDecimal("1212.00");

        System.out.println("\n===== SALÁRIOS MÍNIMOS POR FUNCIONÁRIO =====");
        funcionarios.forEach(funcionario -> {
            BigDecimal quantidadeSalarios = funcionario.getSalario()
                    .divide(salarioMinimo, 2, RoundingMode.HALF_UP);

            System.out.println(funcionario.getNome()
                    + " - "
                    + FORMATO_NUMERO.format(quantidadeSalarios)
                    + " salários mínimos");
        });
    }

    private static void imprimirFuncionario(Funcionario funcionario) {
        System.out.println(
                "Nome: " + funcionario.getNome()
                        + " | Data de nascimento: "
                        + funcionario.getDataNascimento().format(FORMATO_DATA)
                        + " | Salário: R$ "
                        + FORMATO_NUMERO.format(funcionario.getSalario())
                        + " | Função: "
                        + funcionario.getFuncao()
        );
    }
}

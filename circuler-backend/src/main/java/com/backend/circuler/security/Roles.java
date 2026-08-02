package com.backend.circuler.security;

/**
 * Nomes das roles do sistema, em um único lugar.
 *
 * <p>Os <b>nomes das constantes</b> já usam o vocabulário definitivo, enquanto os
 * <b>valores</b> ainda são os antigos, gravados no banco. A divergência é
 * intencional: trocar os valores exige atualizar de uma vez o script SQL, os
 * literais restantes em CollectionPointService e as linhas já existentes na
 * tabela {@code roles} — caso contrário as validações de papel param de casar.
 *
 * <p>Quando os demais domínios forem ajustados, a renomeação se resume a trocar
 * os dois valores abaixo e rodar a migração:
 *
 * <pre>
 * POINT_ADMIN  -&gt; "ROLE_POINT_ADMIN"
 * SYSTEM_ADMIN -&gt; "ROLE_SYSTEM_ADMIN"
 * </pre>
 */
public final class Roles {

    /** Usuário comum. Atribuída automaticamente no cadastro. */
    public static final String USER = "ROLE_USER";

    /** Administra um único Ponto de Coleta: seu acervo, exemplares e reservas. */
    public static final String POINT_ADMIN = "ROLE_ADMIN";

    /** Administra o sistema inteiro. Não é responsável por nenhum ponto. */
    public static final String SYSTEM_ADMIN = "ROLE_ROOT_ADMIN";

    private Roles() {}
}

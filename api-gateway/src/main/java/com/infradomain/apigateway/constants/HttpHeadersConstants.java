package com.infradomain.apigateway.constants;

/**
 * api-gateway
 *
 * @author Juliane Maran
 * @since 12/10/2025
 * <p>
 * Classe utilitária para constantes de cabeçalhos HTTP e valores comuns.
 * <p>
 * Use estas constantes em filtros, configurações de CORS e responses,
 * evitando erros de digitação e melhorando a legibilidade do código.
 */
public final class HttpHeadersConstants {

  // Impede instanciação
  private HttpHeadersConstants() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Cabeçalhos HTTP comuns
   **/
  public static final String AUTHORIZATION = "Authorization";
  public static final String CONTENT_TYPE = "Content-Type";
  public static final String ACCEPT = "Accept";
  public static final String CACHE_CONTROL = "Cache-Control";

  /**
   * Cabeçalhos relacionados a CORS
   **/
  public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
  public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
  public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
  public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
  public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

  /**
   * Cabeçalhos de resposta padrão
   **/
  public static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
  public static final String X_FRAME_OPTIONS = "X-Frame-Options";
  public static final String X_XSS_PROTECTION = "X-XSS-Protection";

  /**
   * Valores comuns
   **/
  public static final String VALUE_CONTENT_TYPE_JSON = "application/json";
  public static final String VALUE_CONTENT_TYPE_XML = "application/xml";
  public static final String VALUE_CONTENT_TYPE_TEXT = "text/plain";

  /**
   * Estratégias e valores genéricos
   **/
  public static final String STRATEGY_RETAIN_FIRST = "RETAIN_FIRST";
  public static final String STRATEGY_RETAIN_LAST = "RETAIN_LAST";

}

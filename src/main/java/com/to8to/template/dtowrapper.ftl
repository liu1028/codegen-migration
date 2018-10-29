package ${basePackage}.dto.dtowrapper;

<#if imports??>
  <#list imports as import>
import ${import};
  </#list>
</#if>

import lombok.Data;

@Data
public class ${dtoClassType} {

<#list dtoElements as el>
    <#list el.validations as validate>
        @${validate}
    </#list>
    private ${el.classType} ${el.classVar};
</#list>

}


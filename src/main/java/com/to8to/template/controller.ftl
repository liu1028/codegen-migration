package ${basePackage}.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.to8to.common.search.PageResult;
import com.to8to.sc.compatible.RPCException;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;

import javax.validation.Valid;

@RestController
<#if baseRequestMapping??>
@RequestMapping("${baseRequestMapping}")
</#if>
@Api(description = "${apiDesciption}")
public class ${className} {

    @Autowired
    private ${serviceClz} ${serviceVar};

<#if methodModels??>
  <#list method as methodModel>
    @PostMapping("${method.requestMapping}")
    @ApiOperation(value = "${method.apiDesc}", response = ${method.responseClassType}.class)
    ResResult<${method.responseClassType}> ${method.name}(@RequestBody @Valid ${method.dtoClassType} ${method.dtoVar}){
        return ResUtils.data(${serviceVar}.${method.name}(<#list method.paramMethods as paramMethod>${method.dtoVar}.${paramMethod}()<#if list_has_next>,</#if></#list>));
    }
  </#list>
</#if>


}
package org.apache.velocity.runtime.parser.node;

/*
 * Copyright 2000-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.util.TemplateNumber;

/**
 * Handles arg1 &lt;= arg2<br><br>
 *
 * Only subclasses of Number can be compared.<br><br>
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @author <a href="mailto:pero@antaramusic.de">Peter Romianowski</a>
 */

public class ASTLENode extends SimpleNode
{
    public ASTLENode(int id)
    {
        super(id);
    }

    public ASTLENode(Parser p, int id)
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    public boolean evaluate( InternalContextAdapter context)
      throws MethodInvocationException
    {
        /*
         *  get the two args
         */
        
        Object left = jjtGetChild(0).value( context );
        Object right = jjtGetChild(1).value( context );

        /*
         *  if either is null, lets log and bail
         */

        if (left == null || right == null)
        {
            rsvc.error( ( left == null ? "Left" : "Right" ) 
                           + " side ("
                           + jjtGetChild( (left == null? 0 : 1) ).literal()
                           + ") of '<=' operation has null value."
                           + " Operation not possible. "
                           + context.getCurrentTemplateName() + " [line " 
                           + getLine() 
                           + ", column " + getColumn() + "]");
            return false;
        }
        
        /*
         *  convert to Number if applicable
         */
        if (left instanceof TemplateNumber) 
        {
           left = ( (TemplateNumber) left).getAsNumber();
        }
        if (right instanceof TemplateNumber) 
        {
           right = ( (TemplateNumber) right).getAsNumber();
        }

        /*
         *  Only compare Numbers
         */
        if ( !( left instanceof Number )  || !( right instanceof Number ))
        {
            rsvc.error( ( !( left instanceof Number ) ? "Left" : "Right" )
                           + " side of '>=' operation is not a Number. "
                           +  context.getCurrentTemplateName() + " [line " + getLine() 
                           + ", column " + getColumn() + "]");
 
            return false;
        }

        return MathUtils.compare ( (Number)left,(Number)right) <= 0;

    }

    public Object value(InternalContextAdapter context)
        throws MethodInvocationException
    {
        boolean val = evaluate(context);

        return val ? Boolean.TRUE : Boolean.FALSE;
    }

}

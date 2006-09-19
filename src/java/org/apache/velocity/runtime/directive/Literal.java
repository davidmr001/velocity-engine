package org.apache.velocity.runtime.directive;

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

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * A very simple directive that leverages the Node.literal()
 * to grab the literal rendition of a node. We basically
 * grab the literal value on init(), then repeatedly use
 * that during render().
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id$
 */
public class Literal extends Directive
{
    String literalText;

    /**
     * Return name of this directive.
     * @return The name of this directive.
     */
    public String getName()
    {
        return "literal";
    }

    /**
     * Return type of this directive.
     * @return The type of this directive.
     */
    public int getType()
    {
        return BLOCK;
    }

    /**
     * Store the literal rendition of a node using
     * the Node.literal().
     * @param rs
     * @param context
     * @param node
     * @throws Exception
     */
    public void init(RuntimeServices rs, InternalContextAdapter context,
                     Node node)
        throws Exception
    {
        super.init( rs, context, node );

        literalText = node.jjtGetChild(0).literal();
    }

    /**
     * Throw the literal rendition of the block between
     * #literal()/#end into the writer.
     * @param context
     * @param writer
     * @param node
     * @return True if the directive rendered successfully.
     * @throws IOException
     */
    public boolean render( InternalContextAdapter context,
                           Writer writer, Node node)
        throws IOException
    {
        writer.write(literalText);
        return true;
    }
}

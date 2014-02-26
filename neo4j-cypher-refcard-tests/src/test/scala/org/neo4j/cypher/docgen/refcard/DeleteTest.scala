/**
 * Copyright (c) 2002-2014 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.docgen.refcard
import org.neo4j.cypher.{ ExecutionResult, StatisticsChecker }
import org.neo4j.cypher.docgen.RefcardTest

class DeleteTest extends RefcardTest with StatisticsChecker {
  val graphDescription = List("ROOT LINK A", "A LINK B", "B LINK C", "C LINK ROOT")
  val title = "DELETE"
  val css = "write c2-2 c4-4 c5-4 c6-3"

  override def assert(name: String, result: ExecutionResult) {
    name match {
      case "delete" =>
        assertStats(result, nodesCreated = 1, deletedNodes = 1, deletedRelationships = 1)
        assert(result.toList.size === 0)
      case "delete-prop" =>
        assertStats(result, nodesCreated = 1, propertiesSet = 2)
        assert(result.toList.size === 0)
    }
  }

  override def parameters(name: String): Map[String, Any] =
    name match {
      case "parameters=set" =>
        Map("value" -> "a value")
      case "parameters=map" =>
        Map("map" -> Map("property" -> "a value"))
      case "" =>
        Map()
    }

  override val properties: Map[String, Map[String, Any]] = Map(
    "A" -> Map("value" -> 10),
    "B" -> Map("value" -> 20),
    "C" -> Map("value" -> 30))

  def text = """
###assertion=delete
START r = relationship(1)
CREATE (n)

DELETE n, r
###

Delete a node and a relationship.

###assertion=delete-prop
CREATE (n {property: "value"})

DELETE n.property
###

Delete a property.
"""
}

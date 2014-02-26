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

class AggregationTest extends RefcardTest with StatisticsChecker {
  val graphDescription = List("ROOT KNOWS A", "A KNOWS B", "B KNOWS C", "C KNOWS ROOT")
  val title = "Aggregation"
  val css = "general c5-5 c6-5"

  override def assert(name: String, result: ExecutionResult) {
    name match {
      case "returns-one" =>
        assertStats(result, nodesCreated = 0)
        assert(result.toList.size === 1)
      case "returns-none" =>
        assertStats(result, nodesCreated = 0)
        assert(result.toList.size === 0)
    }
  }

  override def parameters(name: String): Map[String, Any] =
    name match {
      case "parameters=value" =>
        Map("value" -> "Bob")
      case "parameters=percentile" =>
        Map("percentile" -> 0.5)
      case "" =>
        Map()
    }

  override val properties: Map[String, Map[String, Any]] = Map(
    "A" -> Map("property" -> 10),
    "B" -> Map("property" -> 20),
    "C" -> Map("property" -> 30))

  def text = """
###assertion=returns-one
START n=node(%A%), m=node(%B%)
MATCH path=(n)-->(m)
RETURN NODES(path),

COUNT(*)
###

The number of matching rows.

###assertion=returns-one
START identifier=node(%A%), m=node(%B%)
MATCH path=(identifier)-->(m)
RETURN NODES(path),

COUNT(identifier)
###

The number of non-`null` values.

###assertion=returns-one
START identifier=node(%A%), m=node(%B%)
MATCH path=(identifier)-->(m)
RETURN NODES(path),

COUNT(DISTINCT identifier)
###

All aggregation functions also take the `DISTINCT` modifier, 
which removes duplicates from the values

###assertion=returns-one
START n=node(%A%, %B%, %C%)
RETURN

SUM(n.property)
###

Sum numerical values.

###assertion=returns-one
START n=node(%A%, %B%, %C%)
RETURN

AVG(n.property)
###

Calculates the average.

###assertion=returns-one
START n=node(%A%, %B%, %C%)
RETURN

MAX(n.property)
###

Maximum numerical value.

###assertion=returns-one
START n=node(%A%, %B%, %C%)
RETURN

MIN(n.property)
###

Minimum numerical value.

###assertion=returns-one
START n=node(%A%, %B%, %C%)
RETURN

COLLECT(n.property?)
###

Collection from the values, ignores `null`.

###assertion=returns-one parameters=percentile
START n=node(%A%, %B%, %C%)
RETURN

PERCENTILE_DISC(n.property, {percentile})
###

Discrete percentile.
The `percentile` argument is from `0.0` to `1.0`.

###assertion=returns-one parameters=percentile
START n=node(%A%, %B%, %C%)
RETURN

PERCENTILE_CONT(n.property, {percentile})
###

Continuous percentile.
"""
}

/*
Copyright (c) 2008-2009 Bryan Head
All Rights Reserved

[This software is released under the "MIT License"]

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the
Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall
be included in all copies or substantial portions of the
Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.headb;

/**
 *
 * @author headb
 */
public class SandpileProtocol {

	SandpileController sc;

	public SandpileProtocol(SandpileController sc) {
		this.sc = sc;
	}

	public String processInput(String input) {
		String output = "done";
		System.err.println("Received input");
		String[] command = input.split(" ");
		if (command[0].equals("update")) {
			sc.update();
		} else if (command[0].equals("stabilize")) {
			sc.stabilize();
		} else if (command[0].equals("repaint")) {
			sc.repaint();
		} else if (command[0].equals("delete_graph")) {
			sc.delAllVertices();
		} else if (command[0].equals("clear_sand")) {
			sc.clearSand();
		} else if (command[0].equals("get_vertices")) {
			if (sc.vertexData.isEmpty()) {
				output = "";
			} else {
				StringBuilder sb = new StringBuilder();
				float[] v = sc.vertexData.get(0);
				sb.append(v[0]+" "+v[1]);
				for (int i = 1; i < sc.vertexData.size(); i++) {
					v = sc.vertexData.get(i);
					sb.append("," + v[0] + " " + v[1]);
				}
				output = sb.toString();
			}
		} else if (command[0].equals("get_edges")) {
			output = "";
			for (int v = 0; v < sc.getConfig().size(); v++) {
				StringBuilder sb = new StringBuilder();
				boolean needsComma = false;
				for (int[] e : sc.getGraph().getOutgoingEdges(v)) {
					if (needsComma) {
						sb.append(",");
					} else {
						needsComma = true;
					}
					sb.append(e[0]+" "+e[1]+" "+e[2]);
				}
			}
		} else if (command[0].equals("add_edge")){
			sc.addEdge(Integer.valueOf(command[1]),Integer.valueOf(command[2]), Integer.valueOf(command[3]));
		} else if (command[0].equals("add_edges")){
			
		} else if (command[0].equals("add_vertex")) {
			sc.addVertex(Integer.valueOf(command[0]), Integer.valueOf(command[1]));
		} else if (command[0].equals("add_vertices")) {
			for (int i = 1; i < command.length - 1; i += 1) {
				sc.addVertex(Integer.valueOf(command[0]), Integer.valueOf(command[1]));
			}
		} else if (command[0].equals("add_sand")) {
			int vertex = Integer.valueOf(command[1]);
			int sand = Integer.valueOf(command[2]);
			sc.addSand(vertex, sand);
		} else if (command[0].equals("set_sand")) {
			int vertex = Integer.valueOf(command[1]);
			int sand = Integer.valueOf(command[2]);
			sc.setSand(vertex, sand);
		} else if (command[0].equals("get_sand")) {
			int vertex = Integer.valueOf(command[1]);
			output = String.valueOf(sc.getSand(vertex));
		} else if (command[0].equals("is_sink")) {
			int vertex = Integer.valueOf(command[1]);
			output = String.valueOf(sc.getGraph().isSink(vertex));
		} else if (command[0].equals("add_config")) {
			SandpileConfiguration config = new SandpileConfiguration();
			for (int v = 1; v < command.length; v++) {
				config.add(Integer.valueOf(command[v]));
			}
			sc.addConfig(config);
		} else if (command[0].equals("set_config")) {
			SandpileConfiguration config = new SandpileConfiguration();
			for (int v = 1; v < command.length; v++) {
				config.add(Integer.valueOf(command[v]));
			}
			sc.setConfig(config);
		} else if (command[0].equals("get_config")) {
			output = configToString(sc.getConfig());
		} else if (command[0].equals("get_num_unstables")) {
			output = String.valueOf(sc.getGraph().getUnstables(sc.getConfig()).size());
		} else if (command[0].equals("add_random_sand")) {
			sc.addSandToRandom(sc.getGraph().getNonSinks(), Integer.valueOf(command[1]));
		} else if (command[0].equals("set_to_max_stable")) {
			sc.setToMaxStableConfig();
		} else if (command[0].equals("add_max_stable")) {
			sc.addMaxStableConfig();
		}else if (command[0].equals("get_max_stable")){
			output = configToString(sc.getGraph().getMaxConfig());
		} else if (command[0].equals("set_to_identity")) {
			sc.setToIdentity();
		} else if (command[0].equals("add_identity")) {
			sc.addIdentity();
		}else if (command[0].equals("get_identity")){
			output = configToString(sc.getIdentity());
		} else if (command[0].equals("set_to_burning")) {
			sc.setToBurningConfig();
		} else if (command[0].equals("add_burning")) {
			sc.addBurningConfig();
		}else if (command[0].equals("get_burning")){
			output = configToString(sc.getGraph().getMinimalBurningConfig());
		} else if (command[0].equals("set_to_dual")) {
			sc.setToDualConfig();
		} else if (command[0].equals("add_dual")) {
			sc.addDualConfig();
		}else if (command[0].equals("get_dual")){
			output = configToString(sc.getGraph().getDualConfig(sc.getConfig()));
		} else {
			System.err.println("Could not understand message: " + input);
		}
		System.err.println("Sending output");
		return output;
	}

	public String configToString(SandpileConfiguration config) {
		StringBuilder sb = new StringBuilder();
		if (!config.isEmpty()) {
			sb.append(config.get(0));
			for (int v = 1; v < config.size(); v++) {
				sb.append(" "+config.get(v));
			}
		}
		return sb.toString();
	}
}

/*
 * Copyright (C) 2017 Peadar Grant
 *
 * This program is free software: you can redistribute it and/or modify
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
package marksheet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peadar Grant
 */
public class Part {
    
    private final BigInteger marks;
    private final List<String> rubric;
    
    public Part(BigInteger marks) {
        this.marks = marks;
        rubric = new ArrayList<>();
    }
    
    public void addRubric(String marks, String description) {
        rubric.add(marks + "/" + this.marks + " = " + description);
    }
    
    public BigInteger getMarks() {
        return marks;
    }
    
    public String getCellRubric() {
        StringBuilder sb = new StringBuilder();
        for ( String item : rubric ) {
            sb.append(item);
            sb.append("\n");
        }
        return sb.toString();
    }
}

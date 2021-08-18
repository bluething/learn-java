package io.github.bluething.java.heapdump.simulatememoryoverconsumption;

import com.opencsv.CSVParser;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryHeavyStatisticsService extends HttpServlet {
    private static final int SALES_VOLUME_COL = 9;

    public static void main(String[] args) throws Exception {
        final ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(MemoryHeavyStatisticsService.class, "/");

        final Server server = new Server(9000);
        server.setHandler(servletHandler);
        server.dumpStdErr();
        server.start();
        server.join();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final List<String> lines = readLines(req);
        final List<Integer> salesVolumes = parseSalesVolumes(lines);
        final double mean = mean(salesVolumes);
        printResponse(resp, mean);
    }

    private List<String> readLines(final HttpServletRequest req) throws IOException
    {
        final ServletInputStream inputStream = req.getInputStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return reader.lines().skip(1).collect(Collectors.toList());
    }

    private List<Integer> parseSalesVolumes(final List<String> lines) throws IOException
    {
        final CSVParser csvParser = new CSVParser();
        final List<Integer> salesVolumes = new ArrayList<>();
        for (String line : lines)
        {
            final String[] row = csvParser.parseLine(line);
            final String salesVolumeCell = row[SALES_VOLUME_COL];
            final int salesVolume = salesVolumeCell.isEmpty() ? 0 : Integer.parseInt(salesVolumeCell);
            salesVolumes.add(salesVolume);
        }
        return salesVolumes;
    }

    private double mean(List<Integer> salesVolumes) {
        int total = 0;
        for (Integer volume : salesVolumes)
        {
            total += volume;
        }
        return ((double) total) / salesVolumes.size();
    }

    private void printResponse(HttpServletResponse resp, double mean) throws IOException {
        resp.setContentType("text/plain");
        final Writer writer = new OutputStreamWriter(resp.getOutputStream());
        writer.write("Mean Sales Volume: ");
        writer.write(String.valueOf(mean));
        writer.write('\n');
        writer.close();
    }
}

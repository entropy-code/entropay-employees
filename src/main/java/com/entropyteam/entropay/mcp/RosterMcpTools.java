package com.entropyteam.entropay.mcp;

import java.util.List;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.mcp.dtos.RosterEntry;

@Service
public class RosterMcpTools {

    private final RosterQueryService rosterQueryService;

    public RosterMcpTools(RosterQueryService rosterQueryService) {
        this.rosterQueryService = rosterQueryService;
    }

    @Tool(name = "list_roster",
            description = "List active Entropy employees with name, role, project, client and country.")
    public List<RosterEntry> listRoster() {
        return rosterQueryService.listRoster();
    }
}

package me.neznamy.tab.shared.features.layout;

import me.neznamy.tab.api.TabFeature;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.protocol.PacketPlayOutPlayerInfo;
import me.neznamy.tab.shared.TAB;
import me.neznamy.tab.api.TabConstants;

public class LayoutLatencyRefresher extends TabFeature {

    private final LayoutManager manager;

    public LayoutLatencyRefresher(LayoutManager manager) {
        super(manager.getFeatureName(), "Updating latency");
        this.manager = manager;
        TAB.getInstance().getPlaceholderManager().addUsedPlaceholder(TabConstants.Placeholder.PING, this);
    }

    @Override
    public void refresh(TabPlayer p, boolean force) {
        for (TabPlayer all : TAB.getInstance().getOnlinePlayers()) {
            if (all.getVersion().getMinorVersion() < 8) continue;
            Layout layout = manager.getPlayerViews().get(all);
            if (layout == null) continue;
            PlayerSlot slot = layout.getSlot(p);
            if (slot == null) continue;
            all.sendCustomPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY,
                    new PacketPlayOutPlayerInfo.PlayerInfoData(slot.getUniqueId(), p.getPing())), TabConstants.PacketCategory.LAYOUT_LATENCY);
        }
    }
}

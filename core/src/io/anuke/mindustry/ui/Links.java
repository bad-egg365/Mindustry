package io.anuke.mindustry.ui;

import io.anuke.arc.Core;
import io.anuke.arc.collection.*;
import io.anuke.arc.util.Strings;
import io.anuke.arc.graphics.Color;
import io.anuke.mindustry.graphics.Pal;

public class Links{
    private static LinkEntry[] links;

    private static void createLinks(){
        links = new LinkEntry[]{
//            new LinkEntry("discord", "https://discord.gg/mindustry", Color.valueOf("7289da")),
//            new LinkEntry("changelog", "https://github.com/Anuken/Mindustry/releases", Pal.accent.cpy()),
//            new LinkEntry("trello", "https://trello.com/b/aE2tcUwF", Color.valueOf("026aa7")),
//            new LinkEntry("wiki", "https://mindustrygame.github.io/wiki/", Color.valueOf("0f142f")),
//            new LinkEntry("reddit", "https://www.reddit.com/r/Mindustry/", Color.valueOf("ee593b")),
//            new LinkEntry("itch.io", "https://anuke.itch.io/mindustry", Color.valueOf("fa5c5c")),
//            new LinkEntry("google-play", "https://play.google.com/store/apps/details?id=io.anuke.mindustry", Color.valueOf("689f38")),
//            new LinkEntry("f-droid", "https://f-droid.org/packages/io.anuke.mindustry/", Color.valueOf("026aa7")),
//            new LinkEntry("github", "https://github.com/Anuken/Mindustry/", Color.valueOf("24292e")),
//            new LinkEntry("dev-builds", "https://github.com/Anuken/MindustryBuilds", Color.valueOf("fafbfc")),
            new LinkEntry("liquid-consume", new String[]{"https://github.com/Anuken/Mindustry/compare/master...Quezler:water-o2", "https://github.com/Quezler/Mindustry/tree/water-o2"}, Color.royal.cpy()),
        };
    }

    public static LinkEntry[] getLinks(){
        if(links == null){
            createLinks();
        }

        return links;
    }

    public static class LinkEntry{
        public final String name, title, description;
        public final Color color;
        public final String[] links;

        public LinkEntry(String name, String[] links, Color color){
            this.name = name;
            this.color = color;
            this.description = Core.bundle.getNotNull("link." + name + ".description");
            this.links = links;

            String title = Core.bundle.getOrNull("link." + name + ".title");
            this.title = title != null ? title : Strings.capitalize(name.replace("-", " "));
        }
    }
}

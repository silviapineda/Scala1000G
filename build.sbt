

name := "SelectVariants"

version := "1.0"

scalaVersion := "2.11.8"

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith(".txt")          => MergeStrategy.discard
  case m if m.toLowerCase.endsWith(".gz")          => MergeStrategy.discard
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*")      => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/") => MergeStrategy.filterDistinctLines
  case "reference.conf"                                    => MergeStrategy.concat
  case _                                                   => MergeStrategy.first
}

artifact in (Compile, assembly) ~= { art =>
  art.copy(`classifier` = Some("assembly"))
}

addArtifact(artifact in (Compile, assembly), assembly)
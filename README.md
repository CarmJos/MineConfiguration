```text
   __  ____          _____          ____                    __  _
  /  |/  (_)__  ___ / ___/__  ___  / _(_)__ ___ _________ _/ /_(_)__  ___
 / /|_/ / / _ \/ -_) /__/ _ \/ _ \/ _/ / _ `/ // / __/ _ `/ __/ / _ \/ _ \
/_/  /_/_/_//_/\__/\___/\___/_//_/_//_/\_, /\_,_/_/  \_,_/\__/_/\___/_//_/
                                      /___/
```

# MineConfiguration

[![version](https://img.shields.io/github/v/release/CarmJos/MineConfiguration)](https://github.com/CarmJos/MineConfiguration/releases)
[![License](https://img.shields.io/github/license/CarmJos/MineConfiguration)](https://opensource.org/licenses/MIT)
[![workflow](https://github.com/CarmJos/MineConfiguration/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/MineConfiguration/actions/workflows/maven.yml)
![CodeSize](https://img.shields.io/github/languages/code-size/CarmJos/MineConfiguration)
![](https://visitor-badge.glitch.me/badge?page_id=MineConfiguration.readme)

EasyConfiguration for MineCraft!

开始在 MineCraft 相关服务器平台上**轻松(做)配置**吧！

## 开发

### 项目结构

#### MineConfiguration-CraftBukkit

CraftBukkit系通用依赖，不包含实现部分，请使用 MineConfiguration-Bukkit 或 MineConfiguration-Spigot 。

#### MineConfiguration-Bukkit

适用于Bukkit的版本，包含以Bukkit为基础的其他服务端*(如Spigot、Paper、CatServer)*。

#### MineConfiguration-Spigot

适用于 Spigot(1.18+) 的版本，适配了1.18与更新版本自带的配置文件注释功能，更加稳定。

#### MineConfiguration-Bungee

适用于BungeeCord的版本，可用JSON与YAML格式，但**不支持配置文件注释**。
如需使用配置文件注释，建议使用 [EasyConfiguration-YAML](https://github.com/CarmJos/EasyConfiguration) 。

### 依赖方式

#### Maven 依赖

<details>
<summary>远程库配置</summary>

```xml

<project>
    <repositories>

        <repository>
            <!--采用Maven中心库，安全稳定，但版本更新需要等待同步-->
            <id>maven</id>
            <name>Maven Central</name>
            <url>https://repo1.maven.org/maven2</url>
        </repository>

        <repository>
            <!--采用github依赖库，实时更新，但需要配置 (推荐) -->
            <id>EasyConfiguration</id>
            <name>GitHub Packages</name>
            <url>https://maven.pkg.github.com/CarmJos/MineConfiguration</url>
        </repository>

        <repository>
            <!--采用我的私人依赖库，简单方便，但可能因为变故而无法使用-->
            <id>carm-repo</id>
            <name>Carm's Repo</name>
            <url>https://repo.carm.cc/repository/maven-public/</url>
        </repository>

    </repositories>
</project>
```

</details>

<details>
<summary>通用原生依赖</summary>

```xml

<project>
    <dependencies>

        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>mineconfiguration-bukkit</artifactId>
            <version>[LATEST RELEASE]</version>
            <scope>compile</scope>
        </dependency>

        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>mineconfiguration-spigot</artifactId>
            <version>[LATEST RELEASE]</version>
            <scope>compile</scope>
        </dependency>

        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>mineconfiguration-bungee</artifactId>
            <version>[LATEST RELEASE]</version>
            <scope>compile</scope>
        </dependency>

    </dependencies>
</project>
```

</details>

#### Gradle 依赖

<details>
<summary>远程库配置</summary>

```groovy
repositories {

    // 采用Maven中心库，安全稳定，但版本更新需要等待同步
    mavenCentral()

    // 采用github依赖库，实时更新，但需要配置 (推荐)
    maven { url 'https://maven.pkg.github.com/CarmJos/MineConfiguration' }

    // 采用我的私人依赖库，简单方便，但可能因为变故而无法使用
    maven { url 'https://repo.carm.cc/repository/maven-public/' }
}
```

</details>

<details>
<summary>通用原生依赖</summary>

```groovy

dependencies {

    api "cc.carm.lib:mineconfiguration-bukkit:[LATEST RELEASE]"

    api "cc.carm.lib:mineconfiguration-spigot:[LATEST RELEASE]"

    api "cc.carm.lib:mineconfiguration-bungee:[LATEST RELEASE]"

}
```

## 支持与捐赠

若您觉得本插件做的不错，您可以通过捐赠支持我！

感谢您对开源项目的支持！

<img height=25% width=25% src="https://raw.githubusercontent.com/CarmJos/CarmJos/main/img/donate-code.jpg"  alt=""/>

## 开源协议

本项目源码采用 [GNU LESSER GENERAL PUBLIC LICENSE](https://www.gnu.org/licenses/lgpl-3.0.html) 开源协议。

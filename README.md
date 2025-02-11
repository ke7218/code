# 项目名称
基于vue+springboot构建的社团星校园社团管理系统
## 简介

本项目采用前后端分离的架构，成功实现了一个功能完整、交互友好的校园社团管理系统。在技术选型上，前端使用Vue.js框架配合Element UI组件库构建用户界面，后端采用Spring Boot + MyBatis Plus技术栈，实现了一个高效、可扩展的服务端架构。

## 功能

- 客户端提供用户友好的界面，允许用户与系统进行交互。
- 服务器端处理业务逻辑，并通过 API 提供数据服务。
- 支持用户认证和数据持久化。
- 提供实时数据更新和推送功能。

## 目录结构

- `client/`: 包含客户端代码，使用 Vue.js 框架。
  - `src/`: 源代码目录，包含组件、视图和路由。
  - `public/`: 静态资源目录。
  - `package.json`: 项目的依赖和脚本配置。
- `server/`: 包含服务器端代码，使用 Java。
  - `src/`: 源代码目录，包含控制器、服务和数据访问层。
  - `pom.xml`: Maven 项目的配置文件。

## 安装与运行

1. 克隆仓库：
   ```bash
   git clone https://github.com/ke7218/code.git
   ```

2. 进入项目目录：
   ```bash
   cd code
   ```

3. 客户端安装与运行：
   ```bash
   cd client
   npm install
   npm run serve
   ```

4. 服务器端安装与运行：
   ```bash
   cd server
   mvn clean install
   mvn spring-boot:run
   ```

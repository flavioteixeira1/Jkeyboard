QJoyPad (Clone Java) - Emulador de Joystick para Teclado

Um programa escrito em Java que utiliza a classe JInput e Robot do Java para que eventos de entrada no joystick possam ser replicados no teclado conforme a configuração na interface gráfica.
🎮 Funcionalidades

    Suporte a dois joysticks simultaneamente (Player 1 e Player 2)

    Interface gráfica interativa com feedback visual em tempo real

    Mapeamento customizável de botões e eixos para teclas do teclado

    Detecção automática de joysticks via JInput

    Sistema de perfil para salvar/recuperar configurações

    Feedback visual dos eventos do joystick (botões mudam de cor quando pressionados)

    Configuração rápida clicando nos botões da interface

    Modos de mapeamento: Padrão e Customizado

📋 Pré-requisitos
Java

    Java JDK 8 ou superior

    Maven 3.6 ou superior

Bibliotecas JInput

O JInput requer bibliotecas nativas específicas para cada sistema operacional:
Linux (64-bit)

    libjinput-linux64.so

Windows (64-bit)

    jinput-dx8_64.dll

    jinput-raw_64.dll

Windows (32-bit)

    jinput-dx8.dll

    jinput-raw.dll

    jinput-wintab.dll

macOS

    libjinput-osx.jnilib

🚀 Instalação e Configuração
1. Configuração das Bibliotecas Nativas
Linux:
bash

# Copie a biblioteca para o diretório de bibliotecas do sistema
sudo cp libjinput-linux64.so /usr/lib/
# Ou para o diretório do Java
sudo cp libjinput-linux64.so $JAVA_HOME/lib/

Windows:

Copie os arquivos .dll para:

    Diretório do sistema (C:\Windows\System32)

    Diretório do projeto

    Ou adicione ao PATH do sistema

macOS:
bash

# Copie a biblioteca para o diretório apropriado
sudo cp libjinput-osx.jnilib /Library/Java/Extensions/

2. Compilação com Maven
bash

# Clone o repositório (se aplicável)
git clone <repositorio>
cd <diretorio-do-projeto>

# Compile o projeto
mvn clean compile

# Crie o JAR executável
mvn package

# O JAR será gerado em target/qjoypad-clone-java.jar

3. Estrutura do Projeto Maven
xml

<!-- Exemplo de pom.xml -->
<project>
    <dependencies>
        <dependency>
            <groupId>net.java.jinput</groupId>
            <artifactId>jinput</artifactId>
            <version>2.0.9</version>
        </dependency>
        <dependency>
            <groupId>net.java.jinput</groupId>
            <artifactId>jinput-platform</artifactId>
            <version>2.0.9</version>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.2.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>com.flavioteixeira1.jkeyboard.core.jkeyboard</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

🖥️ Como Executar
Opção 1: Executar diretamente com Java
bash

java -cp "target/classes:lib/*" com.flavioteixeira1.jkeyboard.core.jkeyboard

Opção 2: Executar o JAR gerado
bash

java -jar target/qjoypad-clone-java.jar

Opção 3: Executar via Maven
bash

mvn compile exec:java -Dexec.mainClass="com.flavioteixeira1.jkeyboard.core.jkeyboard"

🎯 Como Usar
1. Conecte seus Joysticks

    Conecte um ou dois joysticks/gamepads USB ao computador

    O programa detectará automaticamente os dispositivos

2. Interface Principal

A interface é dividida em abas:

    Player 1: Configuração para o primeiro joystick

    Player 2: Configuração para o segundo joystick

    Status: Informações sobre os joysticks conectados

3. Configurar Mapeamento
Para botões:

    Clique em qualquer botão na interface (ex: "Botão 1: [Não configurado]")

    Uma janela de captura aparecerá

    Pressione a tecla desejada no teclado

    O mapeamento será salvo automaticamente

Para eixos:

    Clique em um eixo (ex: "Eixo X: [Não configurado]")

    Configure as teclas para direção negativa e positiva separadamente

    Use as setas direcionais ou outras teclas

4. Alternar entre Mapeamentos

    Use o botão "Usar Mapeamento Customizado" para alternar entre:

        Mapeamento Padrão: Configuração pré-definida

        Mapeamento Customizado: Suas configurações personalizadas

5. Perfis

    Importar/Exportar: Salve e carregue configurações

    Salvar/Reverter: Gerencie mudanças na configuração atual

🎮 Mapeamento Padrão
Player 1:

    Botão 0 (A): Z

    Botão 1 (B): X

    Botão 2 (Start): Enter

    Botão 3 (Select): Ctrl

    Eixo X: Setas Esquerda/Direita

    Eixo Y: Setas Cima/Baixo

Player 2:

    Botão 0 (A): Numpad 7

    Botão 1 (B): Numpad 9

    Botão 2 (Start): Numpad 1

    Botão 3 (Select): Numpad 3

    Eixo X: Numpad 4/6

    Eixo Y: Numpad 8/2

🔧 Solução de Problemas
"Erro ao inicializar joystick"

    Verifique se as bibliotecas nativas estão no lugar correto

    Confirme se o joystick está conectado e funcionando

    Execute com permissões de administrador (se necessário)

"Falha no poll do joystick"

    Reconecte o joystick

    Reinicie o programa

    Verifique se outro programa está usando o joystick

Bibliotecas nativas não encontradas

No Linux, você pode precisar instalar:
bash

# Ubuntu/Debian
sudo apt-get install libjinput-jni

# Ou compilar do fonte
wget https://github.com/jinput/jinput/archive/refs/tags/2.0.9.tar.gz
tar -xzf 2.0.9.tar.gz
cd jinput-2.0.9
ant compile
ant compile-native

📁 Estrutura do Código
text

src/main/java/com/flavioteixeira1/jkeyboard/core/
├── jkeyboard.java              # Classe principal
├── MainWindow.java            # Interface gráfica principal
├── JoystickManager.java       # Gerenciamento de joysticks
├── KeyCaptureDialog.java      # Diálogo de captura de teclas
├── ConfigDialog.java          # Diálogo de configuração
├── ButtonConfigDialog.java    # Diálogo de configuração de botões
├── AxisConfigDialog.java      # Diálogo de configuração de eixos
└── test/
    └── JoystickTest.java      # Utilitário de teste de joystick

🛠️ Desenvolvimento
Adicionando Novas Features

    Novos tipos de mapeamento: Estenda JoystickManager

    Novos diálogos: Crie classes que herdam de JDialog

    Persistência: Implemente salvamento em arquivo JSON/XML

    Mais joysticks: Modifique o sistema de singleton

Compilando Alterações
bash

# Limpar e recompilar
mvn clean compile

# Executar testes
mvn test

# Criar nova versão
mvn package

📄 Licença

Este projeto é open-source. Sinta-se livre para modificar e distribuir.
🙏 Créditos

    Desenvolvido por Flavio Teixeira

    Utiliza a biblioteca JInput para detecção de joysticks

    Inspirado no QJoyPad original

    Interface gráfica com Java Swing

🤝 Contribuindo

    Faça um Fork do projeto

    Crie uma branch para sua feature (git checkout -b feature/AmazingFeature)

    Commit suas mudanças (git commit -m 'Add some AmazingFeature')

    Push para a branch (git push origin feature/AmazingFeature)

    Abra um Pull Request

Nota: Certifique-se de que as bibliotecas nativas do JInput estão corretamente instaladas para seu sistema operacional antes de executar o programa.

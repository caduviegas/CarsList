package io.github.caduviegas.carslist.presentation.leads

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.caduviegas.carslist.R
import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.util.CarColor
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeadsScreen(
    navController: NavController,
    viewModel: LeadsViewModel = koinViewModel()
) {
    val leads by viewModel.leads.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchLeads()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_car),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.app_name),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CarColor.Primary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(CarColor.Background)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Aqui são exibidos os seus pedidos de compra de carro (leads). " +
                                "Se o status do lead for \"Aguardando Sincronizar\" você deve aguardar uma rotina automática que sincroniza leads de 1h em 1h. " +
                                "Se o status for \"Sincronizado\" o pedido já foi enviado para nossos servidores e basta aguardar contato de um dos nossos consultores.",
                        color = CarColor.OnSurface,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }
                items(leads) { lead ->
                    LeadCard(lead = lead, onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("car", lead.car)
                        navController.navigate(io.github.caduviegas.carslist.presentation.CarsDestinations.CAR_DETAILS)
                    })
                }
            }
        }
    }

    BackHandler {
        navController.popBackStack()
    }
}

@Composable
fun LeadCard(
    lead: Lead,
    onClick: () -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    val status = lead.status
    val (statusText, statusBg, statusTextColor) = when (status) {
        "SINCRONIZADO" -> Triple(
            "Pedido Feito",
            Color(0xFFD0F5E2),
            Color(0xFF1B5E20)
        )
        "NOVO" -> Triple(
            "Aguardando sincronizar",
            Color(0xFFD0E6FF),
            Color(0xFF0D47A1)
        )
        else -> Triple(
            status,
            CarColor.SecondaryLight,
            CarColor.OnSurface
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(RectangleShape)
                    .background(CarColor.SecondaryLight),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bg_car),
                    contentDescription = "Foto do carro",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 2.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = lead.car.nomeModelo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = CarColor.OnSurface,
                    maxLines = 1
                )
                Text(
                    text = "Ano: ${lead.car.ano}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = CarColor.OnSurface
                )
                Text(
                    text = "Data: ${lead.date.format(formatter)}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = CarColor.OnSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(statusBg)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = statusText,
                        color = statusTextColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}